package com.carrey.selfadaptionviewpager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val mItemViewHeight = ArrayList<Int>()
    private val MIN_SCALE = 0.5f
    private val MIN_ALPHA = 0.99f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewPager = findViewById<ViewPager>(R.id.selfAdapterViewPager) as ViewPager
        initItemsHeight()

        viewPager.setPageTransformer(false) { page, position ->
            val pageWidth = page.getWidth()
            val pageHeight = page.getHeight()

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(1f)

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2)
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2)
                }

                // Scale the page down (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor)
                page.setScaleY(scaleFactor)

                // Fade the page relative to its size.
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA))

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(1f)
            }
        }


        viewPager.adapter = object : PagerAdapter(), SelfAdaptionHeightViewPager.ViewPagerItemInfo {
            override fun getItemViewHeight(position: Int): Int {
                return if (position < mItemViewHeight.size) {
                    mItemViewHeight[position]
                } else 0

            }

            override fun getItemViewSize() = this@MainActivity.mItemViewHeight.size

            override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

            override fun getCount(): Int = 10

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val root = FrameLayout(this@MainActivity)
                val img = ImageView(this@MainActivity)
                img.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (position % 4 + 1) * 100,
                    Gravity.BOTTOM
                )
                img.setImageResource(R.mipmap.ic_launcher)
                img.scaleType = ImageView.ScaleType.FIT_XY

                root.addView(img)
                container.addView(root)
                if (mItemViewHeight[position] <= 0) {
                    root.measure(0, 0)
                    mItemViewHeight[position] = root.measuredHeight
                }
                return root
            }

            // 修正notifyDataSetChanged()后当前View不刷新的问题：https://stackoverflow.com/a/7287121
            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
                container.removeView(`object` as View?)

        }
    }

    private fun initItemsHeight() {

        mItemViewHeight.clear()
        for (i in 0..10) {
            mItemViewHeight.add(0)
        }


    }
}


