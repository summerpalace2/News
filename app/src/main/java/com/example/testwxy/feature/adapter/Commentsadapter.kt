package com.example.testwxy.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.data.model.Comment
import java.text.SimpleDateFormat
import java.util.*
/**
 * description ： TODO:评论页rv适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/3 17:25
 */

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {

    // 将日期格式化工具提出来，避免在 onBindViewHolder 中重复创建对象，优化性能
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        // 使用 parent.context 更加规范
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)

        holder.tvAuthor.text = comment.author
        holder.tvContent.text = comment.content
        holder.tvTime.text = dateFormat.format(Date(comment.time * 1000)) // 注意：知乎时间戳通常需要乘1000
        holder.tvLikes.text = comment.likes.toString()

        Glide.with(holder.itemView.context)
            .load(comment.avatar)
            .placeholder(R.drawable.tou) // 建议加上占位图
            .circleCrop() // 如果需要圆形头像
            .into(holder.ivAvatar)
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
    }

    /**
     * 差分算法：ListAdapter 的核心
     */
    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            // 根据唯一 ID 判断是否是同一个评论
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            // 因为 Comment 是 data class，== 会比较所有字段内容
            return oldItem == newItem
        }
    }
}
