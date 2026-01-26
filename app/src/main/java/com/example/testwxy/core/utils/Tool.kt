package com.example.testwxy.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.testwxy.R
import com.example.testwxy.data.model.LatestNews
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * description ： TODO:通用工具类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 11:12
 */
object Tool {
    fun getCircularBitmap(bitmap: Bitmap): Bitmap {//裁剪成圆形图片
        val size = minOf(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint().apply {
            isAntiAlias = true
        }

        val rect = Rect(0, 0, size, size)

        // 画圆形
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // 设置绘图模式
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output

    }
    fun getPreviousDays(date: String?, daysAgo: Int): String {
        // 设定日期格式
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        // 将输入的日期字符串解析为 LocalDate 对象
        val inputDate = LocalDate.parse(date, formatter)

        // 获取前 daysAgo 天的日期
        val resultDate = inputDate.minusDays(daysAgo.toLong())

        // 返回前 daysAgo 天的日期格式化为字符串
        return resultDate.format(formatter)
    }
    fun getNextDays(date: String, daysAfter: Int): String {
        // 设定日期格式
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        // 将输入的日期字符串解析为 LocalDate 对象
        val inputDate = LocalDate.parse(date, formatter)

        // 获取 daysAfter 天后的日期
        val resultDate = inputDate.plusDays(daysAfter.toLong())

        // 返回 daysAfter 天后的日期格式化为字符串
        return resultDate.format(formatter)
    }
    fun getNextDay(inputDate: String?): String {
        // 定义日期格式化器
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        // 将输入的日期字符串转为 LocalDate 对象
        val date = LocalDate.parse(inputDate, formatter)

        // 计算后一天的日期
        val nextDay = date.plusDays(1)

        // 返回格式化后的后一天日期
        return nextDay.format(formatter)
    }

    fun getTime(): String {
        return when (LocalDateTime.now().hour) {
            in 6..8 -> "早上好"
            in 8..12 -> "上午好"
            in 12..18 -> "下午好"
            in 18..23 -> "晚上好"
            else -> "该睡觉了"
        }
    }

    fun getDate(): String? {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM月dd日")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
    fun getDateNews(): String? {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formattedDate = currentDate.format(formatter)
        return formattedDate
    }
    fun getDateNDaysAgo(n: Long): String {
        val date = LocalDate.now().minusDays(n)
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return date.format(formatter)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    // 从 Base64 字符串恢复 Bitmap
    fun base64ToBitmap(base64String: String): Bitmap {
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    /**
     * 初始化指示器
     */
    fun setupIndicators(count: Int, indicatorLayout: LinearLayout, context: Context) {
        indicatorLayout.removeAllViews()
        if (count <= 0) return

        for (i in 0 until count) {
            val dot = ImageView(context).apply {
                // 默认全部设为不活跃状态
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive))

                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(10, 0, 10, 0) // 间距稍微加大一点，在平板上更协调
                layoutParams = params

                // 初始缩放比例
                scaleX = 0.8f
                scaleY = 0.8f
            }
            indicatorLayout.addView(dot)
        }

        // 初始化完毕后，立即强制设置第一个点为选中态
        // 这样可以避免 ViewPager2 还没回调时，第一个点是缩小的/灰色
        if (indicatorLayout.childCount > 0) {
            setCurrentIndicator(0, indicatorLayout, context)
        }
    }

    /**
     * 切换当前指示器状态
     */
    fun setCurrentIndicator(index: Int, indicatorLayout: LinearLayout, context: Context) {
        val safeIndex = if (indicatorLayout.childCount > 0) index % indicatorLayout.childCount else 0

        for (i in 0 until indicatorLayout.childCount) {
            val imageView = indicatorLayout.getChildAt(i) as ImageView

            // 逻辑判定：是否为当前选中的点
            val isSelected = i == safeIndex

            // 切换图片
            val drawableId = if (isSelected) R.drawable.indicator_active else R.drawable.indicator_inactive
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))

            // 动画目标：活跃点变大(1.2f)，非活跃点恢复正常(0.8f)
            val targetScale = if (isSelected) 1.2f else 0.8f

            // 动画
            imageView.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .alpha(if (isSelected) 1.0f else 0.6f) // 非活跃点设置 0.6 透明度，活跃点 1.0，增加对比度
                .setDuration(200)
                .start()
        }
    }



    //改变story数据类里面的date数据方便后面操作
    fun updateStoriesWithNewsDate(news: LatestNews): LatestNews {
        // 获取 `news` 中的 `date` 字段值
        val date = news.date

        // 使用 `map` 遍历 `stories` 列表，并将 `news.date` 赋给每个 `Story` 的 `date`
        val updatedStories = news.stories.map { story ->
            story.copy(date = date)  // 更新 `Story` 的 `date` 字段
        }
        Log.d("updateStories",  "$updatedStories")
        // 创建一个新的 `News` 对象，更新 `stories` 为修改后的列表
        return news.copy(stories = updatedStories)
    }





}
