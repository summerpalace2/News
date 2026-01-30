package com.example.testwxy.feature

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import kotlin.math.abs

/*
1,系统级手势兼容： “通过逻辑判定避开了屏幕两侧 40dp 的边缘区域，确保在支持自定义水平滑动切换页面的同时，
不干扰 Android 全面屏手势的‘侧滑返回’功能，解决了用户交互层面的最高优先级冲突。”
2,多维意图识别： “利用动态斜率判定（X轴位移 > Y轴位移 1.6倍）
来精准区分用户的意图——是想‘垂直滚动网页’还是‘横向切换文章’，有效降低了误触率。”
3,父子容器协同(预处理逻辑实际在这里没有用到)： “利用 requestDisallowInterceptTouchEvent() 动态接管事件流，解决了 WebView 嵌套在滑动容器中时可能产生的事件抢夺问题。”
 */

class CustomWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    var onSwipeLeft: (() -> Unit)? = null  // 切换下一篇
    var onSwipeRight: (() -> Unit)? = null // 切换上一篇

    private var startX = 0f
    private var startY = 0f
    private var isSwipingHorizontally = false

    // 滑动阈值和边缘保护区
    private val swipeThreshold = 100 // 触发切换文章的最小距离
    private val edgeThreshold = 40.dpToPx() // 边缘不拦截区：左右两侧各40dp

    companion object {
        private fun Int.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                isSwipingHorizontally = false

                //如果在边缘区域，立即放弃所有后续事件处理，交给系统手势
                if (isInEdgeArea(startX)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                // 不消费DOWN，后续MOVE/UP都不会再传给此View
                }

                //正常区域点击，先阻止父容器拦截（如ViewPager等）
                //当前项目并没有对应的父容器这里可以省略
                //parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                //防御性检查：如果是边缘起点，不处理
                if (isInEdgeArea(startX)) return false

                val deltaX = x - startX
                val deltaY = y - startY

                // 判定是否为“水平滑动切换文章”的意图
                // 条件：水平位移超过阈值，且水平位移明显大于垂直位移（防止斜划误触）
                if (!isSwipingHorizontally && abs(deltaX) > swipeThreshold && abs(deltaX) > abs(deltaY) * 1.6) {
                    isSwipingHorizontally = true
                    // 确定是我们要的滑动，此时彻底接管事件
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isSwipingHorizontally) {
                    val deltaX = x - startX
                    if (abs(deltaX) > swipeThreshold) {
                        if (deltaX > 0) {
                            onSwipeRight?.invoke()
                        } else {
                            onSwipeLeft?.invoke()
                        }
                    }
                    isSwipingHorizontally = false
                    return true
                // 消费掉切换文章的滑动，不让WebView跳转网页链接
                }
                isSwipingHorizontally = false
            }
        }

        // 如果不是我们的水平滑动逻辑，交给系统WebView处理网页内的滚动或点击
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // 在拦截层就避开边缘，确保系统手势有最高优先级
        if (event.action == MotionEvent.ACTION_DOWN && isInEdgeArea(event.x)) {
            return false
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun isInEdgeArea(x: Float): Boolean {
        val screenWidth = resources.displayMetrics.widthPixels
        // 同时检测左边缘和右边缘（全面屏手势两侧都能返回）
        return x < edgeThreshold || x > (screenWidth - edgeThreshold)
    }
}