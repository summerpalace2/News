package com.example.testwxy.repository

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/5 16:52
 */
data class Comments(
    val comments: List<Comment>
)

data class Comment(
    val author: String,
    val avatar: String,
    val content: String,
    val id: Int,
    val likes: Int,
    val reply_to: ReplyTo,
    val time: Long
)

data class ReplyTo(
    val author: String,
    val content: String,
    val id: Int,
    val status: Int
)
sealed class CommentsResult<out T> {
    data class Success<out T>(val data:T):CommentsResult<T>()
    data class Error(val exception:Exception):CommentsResult<Nothing>()
}