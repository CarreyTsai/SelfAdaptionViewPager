package com.carrey.selfadaptionviewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.IllegalArgumentException

/**
 * Created by carrey on 2018/10/16.
 */
class SelfAdaptionHeightViewPager(context: Context, attributeSet: AttributeSet?) : ViewPager(context, attributeSet) {


    private var lastHeight = 0
    private var scrollState = 0
    private var defaultItemHeight = 100

    private var itemInfo: ViewPagerItemInfo? = null
    var resetHeight = false


    init {
        addOnPageChangeListener(object : SimpleOnPageChangeListener() {

            override fun onPageScrollStateChanged(state: Int) {
                scrollState = state
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    resetHeight = false
                }

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 抬手以后进行
                if (scrollState != ViewPager.SCROLL_STATE_SETTLING) {
                    return
                }
                if (resetHeight) {
                    return
                }
                if (itemInfo == null || position >= itemInfo!!.getItemViewSize() - 1) {
                    return
                }

                var height = 0
                val pre = itemInfo!!.getItemViewHeight(position)
                val next = itemInfo!!.getItemViewHeight(position + 1)
                if (positionOffset > 0.8) {
                    height = if (next == 0) defaultItemHeight else next
                } else if (positionOffset < 0.3) {
                    height = if (pre == 0) defaultItemHeight else pre
                }


                //计算ViewPager现在应该的高度,heights[]表示页面高度的数组。
//                height = (int) ((pre == 0 ? defaultItemHeight : pre) * (1 - positionOffset) + (next == 0 ? defaultItemHeight : next) * positionOffset);
                Log.d("ss", height.toString())
                if (height != 0 && lastHeight != height) {
                    lastHeight = height
                    //为ViewPager设置高度
                    val params = layoutParams
                    params.height = height
                    requestLayout()
                    resetHeight = true
                }
            }
        })

    }

    constructor(context: Context) : this(context, null)


    override fun setAdapter(adapter: PagerAdapter?) {

        if (adapter is ViewPagerItemInfo) {
            super.setAdapter(adapter)
            itemInfo = adapter
        } else {
            throw IllegalArgumentException("let your adapter implements ViewPagerItemInfo")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecCopy = heightMeasureSpec

        val index = currentItem
        val view = adapter?.instantiateItem(this, index) as View

        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        var height = view.measuredHeight
        if (height != 0) heightMeasureSpecCopy = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpecCopy)
    }

    open interface ViewPagerItemInfo {

        fun getItemViewHeight(position: Int): Int

        fun getItemViewSize(): Int
    }
}