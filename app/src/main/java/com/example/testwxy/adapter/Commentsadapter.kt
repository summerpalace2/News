package com.example.testwxy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.repository.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/5 17:25
 */
class CommentsAdapter(
    private val comments: List<Comment>
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.tvAuthor.text = comment.author
        holder.tvContent.text = comment.content
        holder.tvTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(comment.time))
        holder.tvLikes.text = comment.likes.toString()
        Glide.with(holder.itemView.context)
            .load(comment.avatar)
            .into(holder.ivAvatar)
    }

    override fun getItemCount(): Int = comments.size

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
         val tvContent: TextView = itemView.findViewById(R.id.tvContent)
         val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
         val tvTime: TextView = itemView.findViewById(R.id.tvTime)
         val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)

            // 使用Glide加载头像


    }
}

