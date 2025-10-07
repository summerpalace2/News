package com.example.testwxy.feature.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testwxy.data.model.Commentinfo
import com.example.testwxy.data.model.CommentsDetail
import com.example.testwxy.data.model.LatestNews
import com.example.testwxy.data.model.Resource
import com.example.testwxy.data.repository.MainRepository
import kotlinx.coroutines.launch

/**
 * description ： TODO:Mainactivity的Viewmdoel
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 16:28
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainRepository()

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
}