package com.example.testwxy.core.network

import com.example.testwxy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * description ： TODO:网络请求封装类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2026/1/23 20:55
 */

object ZhihuApiGenerator {

    // 如果以后有测试环境，可以在这里通过编译配置切换
    private const val BASE_URL = "https://news-at.zhihu.com/"

    // 懒加载 Retrofit 实例，确保线程安全
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        // 日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                // 统一添加请求头
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("User-Agent", "ZhihuDaily-Android/${BuildConfig.VERSION_NAME}")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .method(originalRequest.method, originalRequest.body)

                chain.proceed(requestBuilder.build())
            }
            // 细化超时控制
            .connectTimeout(15, TimeUnit.SECONDS) // 连接超时：建议不宜过长
            .readTimeout(20, TimeUnit.SECONDS)    // 读取超时
            .writeTimeout(20, TimeUnit.SECONDS)   // 写入超时
            .retryOnConnectionFailure(true) // 失败尝试重连
            .build()
    }


     inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }

}