package com.example.testwxy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testwxy.repository.Comment
import com.example.testwxy.repository.Commentnumber
import com.example.testwxy.repository.CommentnumberResult
import com.example.testwxy.repository.Comments
import com.example.testwxy.repository.CommentsResult
import com.example.testwxy.repository.MainRepository
import com.example.testwxy.repository.News
import com.example.testwxy.repository.ResultData
import com.example.testwxy.repository.ResultRecentData
import com.example.testwxy.repository.ResultUrlRecentData
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


    private val _recentNewsResult=MutableLiveData<ResultRecentData<News>>()
    val recentNewsResult:LiveData<ResultRecentData<News>> =_recentNewsResult


    private val _resulturlRecentNewsResult=MutableLiveData<ResultUrlRecentData<News>>()
    val resulturlRecentNewsResult:LiveData<ResultUrlRecentData<News>> =_resulturlRecentNewsResult

    private val _commentnumberResult=MutableLiveData<CommentnumberResult<Commentnumber>>()
    val commentnumberResult:LiveData<CommentnumberResult<Commentnumber>> =_commentnumberResult

    private val _commentResult=MutableLiveData<CommentsResult<Comments>>()
    val commentResult:LiveData<CommentsResult<Comments>> =_commentResult
    fun getNews() {
        viewModelScope.launch {
            _newsResult.value = repository.getNews()
        }
    }
    fun getRecentNews(date:String){
        viewModelScope.launch {
            Log.d("MainViewmodeldate",date)
            _recentNewsResult.value=repository.getRecentNews(date)
        }
    }

    fun getRecentNewsUrl(date:String){
        viewModelScope.launch {
            _resulturlRecentNewsResult.value=repository.getRecentNewsUrl(date)
        }
    }
    fun getnumberComment(id:Int){
        viewModelScope.launch {
            _commentnumberResult.value=repository.getCommentnumber(id)
        }
    }

    fun getComment(id:Int){
        viewModelScope.launch {
            _commentResult.value=repository.getComment(id)
        }
    }




}