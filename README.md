# SelfAdaptionViewPager
自适应高度viewpager


需求:在页面的底部添加一个卡片。卡片内容根据api返回。所以高度是不固定的。需要卡片底定位在底部，并且卡片高度以外的地方不被遮住。
<iframe width="560" height="315" src="https://github.com/CarreyTsai/SelfAdaptionViewPager/blob/master/imgs/self-adaption_height.mp4" frameborder="0" allowfullscreen></iframe>
一个函数搞定：

 我们在view 测量的时候 计算当前显示的子view 的高度来设置viewpager 的高度

```
 override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecCopy = heightMeasureSpec

        val index = currentItem
        val view = adapter?.instantiateItem(this, index) as View

        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        var height = view.measuredHeight
        if (height != 0) heightMeasureSpecCopy = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpecCopy)
    }
```

>遇到的问题： 子view中有个条目或者其他的内容 需要页面绘制完成后来判断是非显示。 当你使用postRunable 去解决这个问题的时候。会影响 viewpager 的高度。 因为view绘制完成后 viewpager 的高度已经设定。 这个时候再隐藏或者显示某个子view。viewpager 的高度 不会重新计算
