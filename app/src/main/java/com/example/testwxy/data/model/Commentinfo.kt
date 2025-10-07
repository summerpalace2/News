package com.example.testwxy.data.model

/**
 * description ： TODO:评论点赞数据类
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/4 23:23
 */
data class Commentinfo(
    val id:Int,
    val comments: Int,
    val long_comments: Int,
    val popularity: Int,
    val short_comments: Int
)
sealed class CommentinfoResult<out T>{
    data class Success<out T>(val data:T):CommentinfoResult<T>()
    data class Error(val exception:Exception):CommentinfoResult<Nothing>()
}