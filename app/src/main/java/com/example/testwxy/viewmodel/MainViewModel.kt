package com.example.testwxy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testwxy.repository.MainRepository
import com.example.testwxy.repository.News
import com.example.testwxy.repository.ResultData
import kotlinx.coroutines.launch

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 16:28
 */
class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository by lazy { MainRepository() }

    private val _newsResult=MutableLiveData<ResultData<News>>()
    val newsResult:LiveData<ResultData<News>> =_newsResult

    fun getNews(){
        viewModelScope.launch {
            _newsResult.value=repository.getNews()
        }
    }
}