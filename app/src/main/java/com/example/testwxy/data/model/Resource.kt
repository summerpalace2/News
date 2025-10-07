package com.example.testwxy.data.model

/**
 * description ： TODO:result通用密闭类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2026/1/26 00:01
 */

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>() // 还可以增加加载中状态
}