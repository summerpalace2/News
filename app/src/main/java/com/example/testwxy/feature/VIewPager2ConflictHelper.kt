package com.example.testwxy.feature
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
/**
 * description ： TODO:专门解决 ViewPager2 嵌套滑动冲突的辅助工具
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2026/1/26 20:39
 */
object ViewPager2ConflictHelper {

    /**
     * 为 ViewPager2 绑定冲突处理逻辑
     */
    fun handleConflict(viewPager: ViewPager2) {
        val innerRecyclerView = viewPager.getChildAt(0) as? RecyclerView ?: return

        innerRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            private var startX = 0f
            private var startY = 0f

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = e.x
                        startY = e.y
                        // 按下时锁定父容器
                        rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val endX = e.x
                        val endY = e.y
                        val disX = abs(endX - startX)
                        val disY = abs(endY - startY)

                        // 核心判定：如果是横向滑动意图（X位移 > Y位移）
                        if (disX > disY) {
                            // 依然锁定父容器，ViewPager2 自己消费
                            rv.parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            // 如果是纵向滑动意图，释放拦截权，交给 RecyclerView 或 SwipeRefreshLayout
                            rv.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        rv.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }
}