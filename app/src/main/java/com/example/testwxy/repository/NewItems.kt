package com.example.testwxy.repository

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/4 11:07
 */
// NewsItem.kt
sealed class NewsItems {
    data class DateHeader(val date: String): NewsItems()
    data class StoryItem(val story:Story): NewsItems()
    data class Footer(val hint: String): NewsItems()
}

 fun buildMixedList(batches: List<Story>, date: String): List<NewsItems> {
    val mixed = mutableListOf<NewsItems>()

     mixed.add(NewsItems.DateHeader(date))
     for (story in batches) {
         mixed.add(NewsItems.StoryItem(story))
     }
     mixed.add(NewsItems.Footer(""))
    return mixed
}