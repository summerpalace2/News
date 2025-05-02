package com.example.testwxy.tool

import android.app.AlertDialog
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
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
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
    fun getCircularBitmap(bitmap: Bitmap): Bitmap {
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

}
