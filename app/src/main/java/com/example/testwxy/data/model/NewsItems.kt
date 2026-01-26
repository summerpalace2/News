package com.example.testwxy.data.model


/**
 * description ： TODO:主页adapter的混合rv数据类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/4 11:07
 */
sealed class NewsItems {
    // 轮播图头部，持有 TopStory 列表
    data class BannerHeader(val topStories: List<TopStory>) : NewsItems()
    // 日期分割线
    data class DateHeader(val date: String) : NewsItems()
    // 普通新闻条目
    data class StoryItem(val story: Story) : NewsItems()
    // 底部加载占位
    object Footer : NewsItems()
}

/**
 * 辅助方法：构建混合列表
 */
fun buildMixedList(batches: List<Story>, date: String, topStories: List<TopStory>? = null): List<NewsItems> {
    val mixed = mutableListOf<NewsItems>()

    // 只有在提供 topStories 时（即首页第一屏）才添加 BannerHeader
    topStories?.let {
        if (it.isNotEmpty()) {
            mixed.add(NewsItems.BannerHeader(it))
        }
    }

    mixed.add(NewsItems.DateHeader(date))
    for (story in batches) {
        mixed.add(NewsItems.StoryItem(story))
    }
    mixed.add(NewsItems.Footer)
    return mixed
}