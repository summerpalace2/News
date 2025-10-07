package com.example.testwxy.feature.news

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.databinding.ActivityDetailBinding

/**
 * description ： TODO:轮播图跳转页
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 18:28
 */
class BannerDetailActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        val url = intent.getStringExtra("url") ?: "https://example.com"
        Log.d("wwwurl", url)

        // 关键：给 WebView 配置基础参数
        val webSettings = binding.webView.settings
        // 启用 JavaScript
        webSettings.javaScriptEnabled = true
        //支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        // 允许网页缩放
        webSettings.setSupportZoom(true)
        // 处理 HTTPS 混合内容（如果网站有 HTTP 资源）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }


        // 加载 URL
        binding.webView.loadUrl(url)
    }
}