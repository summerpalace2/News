package com.example.testwxy.repository

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/3 12:42
 */


sealed class ResultRecentData<out T> {
    data class Success<out T>(val data: T) : ResultRecentData<T>()
    data class Error(val exception: Exception) : ResultRecentData<Nothing>()
}

sealed class ResultUrlRecentData<out T> {
    data class Success<out T>(val data: T) : ResultUrlRecentData<T>()
    data class Error(val exception: Exception) : ResultUrlRecentData<Nothing>()
}