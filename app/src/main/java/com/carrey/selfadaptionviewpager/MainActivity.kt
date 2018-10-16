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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewPager = findViewById<ViewPager>(R.id.selfAdapterViewPager)

        viewPager.adapter = object : PagerAdapter() {
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
                return root
            }

            // 修正notifyDataSetChanged()后当前View不刷新的问题：https://stackoverflow.com/a/7287121
            override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
                container.removeView(`object` as View?)

        }
    }
}
