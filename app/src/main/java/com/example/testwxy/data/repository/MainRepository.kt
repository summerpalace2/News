package com.example.testwxy.data.repository

import com.example.testwxy.core.network.ZhihuApiGenerator
import com.example.testwxy.data.model.Commentinfo
import com.example.testwxy.data.model.CommentsDetail
import com.example.testwxy.data.model.LatestNews
import com.example.testwxy.data.model.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * description ：
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 15:50
 */

class MainRepository {

    // 使用 ApiGenerator 单例创建服务
    private val apiService = ZhihuApiGenerator.createService<NewsService>()

    /**
     *通用的安全 API 调用工具函数
     * 利用泛型 T 自动处理不同的响应数据类型
     */
    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error(Exception("服务器返回数据为空"))
            } else {
                Resource.Error(Exception("网络请求失败: ${response.code()}"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    // --- 以下是精简后的请求方法 ---

    // 获取最新新闻
    suspend fun getNews() = safeApiCall { apiService.getNews() }

    // 获取历史新闻
    suspend fun getRecentNews(date: String) = safeApiCall { apiService.getRecentNews(date) }

    // 获取新闻额外信息（点赞数、评论数）
    suspend fun getCommentNumber(id: Int) = safeApiCall { apiService.getCommentNumber(id) }

    // 获取新闻评论详情
    suspend fun getComments(id: Int) = safeApiCall { apiService.getComments(id) }
}

/**
 * 修正后的 API 接口定义
 */
interface NewsService {
    @GET("api/4/news/latest")
    suspend fun getNews(): Response<LatestNews>

    // 获取某个日期之前的新闻
    @GET("api/4/news/before/{date}")
    suspend fun getRecentNews(@Path("date") date: String): Response<LatestNews>

    @GET("api/4/story-extra/{id}")
    suspend fun getCommentNumber(@Path("id") id: Int): Response<Commentinfo>

    @GET("api/4/story/{id}/short-comments")
    suspend fun getComments(@Path("id") id: Int): Response<CommentsDetail>
}