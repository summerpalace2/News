package com.example.testwxy.data.model

/**
 * description ： TODO:知乎日报最近新闻数据类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 15:59
 */
data class LatestNews(
    val date: String,
    val stories: List<Story>,
    val top_stories: List<TopStory>?=null
)

data class Story(
    var date: String,
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
    var date:String,
    val ga_prefix: String,
    val hint: String,
    val id: Int?,
    val image: String,
    val image_hue: String,
    val title: String,
    val type: Int,
    val url: String
)
