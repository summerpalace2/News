package com.example.testwxy.repository

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/4 23:23
 */
data class Commentnumber(
    val id:Int,
    val comments: Int,
    val long_comments: Int,
    val popularity: Int,
    val short_comments: Int
)
sealed class CommentnumberResult<out T>{
    data class Success<out T>(val data:T):CommentnumberResult<T>()
    data class Error(val exception:Exception):CommentnumberResult<Nothing>()
}