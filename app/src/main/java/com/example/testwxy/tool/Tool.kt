package com.example.testwxy.tool

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.testwxy.R
import com.example.testwxy.repository.News
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * description ： TODO:类的作用
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
    fun getPreviousDays(date: String, daysAgo: Int): String {
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
    fun getNextDay(inputDate: String): String {
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
    fun setupIndicators(count: Int, indicatorLayout: LinearLayout, context: Context) {
        indicatorLayout.removeAllViews()//删除之前设置的防止干扰
        for (i in 0 until count) {
            val dot = ImageView(context).apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive))//设置小圆点的样式
                val params = LinearLayout.LayoutParams(//表示该视图（ImageView）将被添加到一个 LinearLayout 中，因此使用它的专用布局参数类
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT//表示宽度和高度都是根据内容自适应大小（即小圆点的图片大小）
                )
                params.setMargins(8, 0, 8, 0)//设置间距
                layoutParams = params
                scaleX = 0.5f // 初始缩小一点，未选中状态
                scaleY = 0.5f
            }
            indicatorLayout.addView(dot)
        }
    }


    fun setCurrentIndicator(index: Int, indicatorLayout: LinearLayout, context: Context) {
        for (i in 0 until indicatorLayout.childCount) {
            val imageView = indicatorLayout.getChildAt(i) as ImageView //获取每个小圆点对应的图案

            val drawableId = if (i == index) R.drawable.indicator_active else R.drawable.indicator_inactive//设置选中和没有选中的情况
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))

            val targetScale = if (i==index) 1.2f else 0.8f

            // 动画：逐渐缩放
            imageView.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .setDuration(250)
                .start()
        }
    }

    fun updateStoriesWithNewsDate(news: News): News {//改变story数据类里面的date数据方便后面操作
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
