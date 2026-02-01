package com.example.testwxy.feature.news

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwxy.data.model.Commentinfo
import com.example.testwxy.data.model.CommentsDetail
import com.example.testwxy.data.model.LatestNews
import com.example.testwxy.data.model.NewsItems
import com.example.testwxy.data.model.Resource
import com.example.testwxy.data.repository.MainRepository
import kotlinx.coroutines.launch

/**
 * description ： TODO:Mainactivity的Viewmdoel
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 16:28
 */

class MainViewModel : ViewModel() {

    private val repository = MainRepository()
    val fullNewsList = mutableStateListOf<NewsItems>()

    private var oldestDate = ""
    var isPagingLoading = false


    // 所有的结果都观察 Resource<数据模型>
    private val _newsResult = MutableLiveData<Resource<LatestNews>>()
    val newsResult: LiveData<Resource<LatestNews>> = _newsResult

    private val _recentNewsResult = MutableLiveData<Resource<LatestNews>>()
    val recentNewsResult: LiveData<Resource<LatestNews>> = _recentNewsResult

    private val _commentInfoResult = MutableLiveData<Resource<Commentinfo>>()
    val commentInfoResult: LiveData<Resource<Commentinfo>> = _commentInfoResult

    private val _commentListResult = MutableLiveData<Resource<CommentsDetail>>()
    val commentListResult: LiveData<Resource<CommentsDetail>> = _commentListResult

    // 统一的调用简写
    fun getNews() = viewModelScope.launch {
        _newsResult.value = repository.getNews()
    }

    fun getRecentNews(date: String) = viewModelScope.launch {
        _recentNewsResult.value = repository.getRecentNews(date)
    }

    fun getCommentInfo(id: Int) = viewModelScope.launch {
        _commentInfoResult.value = repository.getCommentNumber(id)
    }

    fun getComments(id: Int) = viewModelScope.launch {
        _commentListResult.value = repository.getComments(id)
    }

    fun handleInitialNews(data: LatestNews) {
        oldestDate = data.date

        // 转换数据模型
        val mixedList = mutableListOf<NewsItems>()
        data.top_stories?.let { mixedList.add(NewsItems.BannerHeader(it)) }
        mixedList.add(NewsItems.DateHeader(data.date))
        data.stories.forEach {
            it.date = data.date
            mixedList.add(NewsItems.StoryItem(it))
        }

        fullNewsList.clear() // 下拉刷新或首次进入，重置数据
        fullNewsList.addAll(mixedList)
        isPagingLoading = false
    }


    // 历史新闻追加逻辑：纯粹追加，不删任何东西
    fun handleHistoryNews(data: LatestNews) {
        val historyBatch = mutableListOf<NewsItems>()
        historyBatch.add(NewsItems.DateHeader(data.date))
        data.stories.forEach {
            it.date = data.date
            historyBatch.add(NewsItems.StoryItem(it))
        }

        // 使用 Snapshot 包装，保证 remove 和 add 合并为一个原子操作
        androidx.compose.runtime.snapshots.Snapshot.withMutableSnapshot {
            // 先找到 Footer 的位置（如果存在）
            val footerIndex = fullNewsList.indexOfFirst { it is NewsItems.Footer }
            if (footerIndex != -1) {
                // 在 Footer 的位置直接插入新数据，然后再删掉 Footer
                // 或者直接用新数据替换掉 Footer
                fullNewsList.removeAt(footerIndex)
            }
            fullNewsList.addAll(historyBatch)
        }

        oldestDate = data.date
        isPagingLoading = false
    }

    // 5. 触发加载更多
    fun loadMore() {
        if (!isPagingLoading && oldestDate.isNotEmpty()) {
            isPagingLoading = true
            fullNewsList.add(NewsItems.Footer) // 添加脚布局
            getRecentNews(oldestDate) // 调用仓库请求
        }
    }


}

