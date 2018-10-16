package com.carrey.selfadaptionviewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

/**
 * Created by carrey on 2018/10/16.
 */
class SelfAdaptionHeightViewPager(context: Context, attributeSet: AttributeSet?) : ViewPager(context, attributeSet) {

    constructor(context: Context) : this(context, null)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecCopy = heightMeasureSpec

        val index = currentItem
        val view = adapter?.instantiateItem(this, index) as View

        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        var height = view.measuredHeight
        if (height != 0) heightMeasureSpecCopy = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpecCopy)
    }
}