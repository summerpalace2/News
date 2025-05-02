package com.example.testwxy.repository

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 15:59
 */
data class News(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>
)

data class Story(
    val ga_prefix: String,
    val hint: String,
    val id: Int,
    val image_hue: String,
    val images: List<String>,
    val title: String,
    val type: Int,
    val url: String
)

data class TopStory(
    val ga_prefix: String,
    val hint: String,
    val id: Int,
    val image: String,
    val image_hue: String,
    val title: String,
    val type: Int,
    val url: String
)
sealed class ResultData<out T> {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Exception) : ResultData<Nothing>()

}