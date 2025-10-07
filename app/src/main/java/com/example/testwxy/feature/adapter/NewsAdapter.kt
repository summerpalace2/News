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
import com.example.testwxy.data.model.NewsItems
import com.example.testwxy.data.model.Story

/**
 * description ： TODO:主页rv适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/5 17:25
 */
class NewsAdapter(
    private val onClick: (Story) -> Unit
) : ListAdapter<NewsItems, RecyclerView.ViewHolder>(NewsDiffCallback()) {

    private val TYPE_FOOTER = 0
    private val TYPE_DATA = 1
    private val TYPE_DATE = 2

    // --- ViewHolder 内部获取 Context 统一使用 holder.itemView.context ---

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_News_title)
        val author: TextView = itemView.findViewById(R.id.tv_News_author)
        val image: ImageView = itemView.findViewById(R.id.tv_News_image)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position) // ListAdapter 提供的内置方法
                    if (item is NewsItems.StoryItem) onClick(item.story)
                }
            }
        }
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NewsItems.DateHeader -> TYPE_DATE
            is NewsItems.StoryItem -> TYPE_DATA
            is NewsItems.Footer -> TYPE_FOOTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 【标准操作】：使用 parent.context 确保主题一致且无内存泄漏
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_DATA -> NewsViewHolder(inflater.inflate(R.layout.item_rv, parent, false))
            TYPE_DATE -> DateViewHolder(inflater.inflate(R.layout.item_date, parent, false))
            else -> FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is NewsViewHolder -> {
                val story = (item as NewsItems.StoryItem).story
                holder.title.text = story.title
                holder.author.text = story.hint
                // Glide 直接使用 itemView 的 context 即可
                Glide.with(holder.itemView.context)
                    .load(story.images.getOrNull(0))
                    .into(holder.image)
            }
            is DateViewHolder -> {
                holder.date.text = (item as NewsItems.DateHeader).date
            }
        }
    }

    /**
     * 差分算法逻辑：定义如何判断两个 Item 是同一个东西，以及内容是否有变
     */
    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItems>() {
        override fun areItemsTheSame(oldItem: NewsItems, newItem: NewsItems): Boolean {
            return when {
                oldItem is NewsItems.StoryItem && newItem is NewsItems.StoryItem ->
                    oldItem.story.id == newItem.story.id
                oldItem is NewsItems.DateHeader && newItem is NewsItems.DateHeader ->
                    oldItem.date == newItem.date
                oldItem is NewsItems.Footer && newItem is NewsItems.Footer -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: NewsItems, newItem: NewsItems): Boolean {
            // NewsItems 是 data class，所以 == 会自动比对所有成员变量
            return oldItem == newItem
        }
    }
}