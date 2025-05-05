package com.example.testwxy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.repository.News
import com.example.testwxy.repository.NewsItems

import com.example.testwxy.repository.Story
import kotlin.math.log

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 21:49
 */
class NewsAdapter (
    private val context: Context,
    private var newsList:List<NewsItems>,
    private val onClick:(Story) ->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_FOOTER = 0
    private val DATA = 1
    private val DATE = 2
        inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_News_title)
            val author: TextView = itemView.findViewById(R.id.tv_News_author)
            val image: ImageView = itemView.findViewById(R.id.tv_News_image)
        }
        inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        }
        inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val date: TextView = itemView.findViewById(R.id.date)
        }
    fun updateData(newList: List<NewsItems>) {
        this.newsList = newList
        notifyDataSetChanged()
    }
    fun appendData(moreItems: List<NewsItems>) {
        if (newsList.isNotEmpty()) {
            newsList = newsList.dropLast(1) // 删除最后一项
           notifyItemRemoved(newsList.size) // 更新 RecyclerView
       }
         //追加新数据
        val oldSize = newsList.size
        newsList = newsList + moreItems  // 新建一个新列表
        notifyItemRangeInserted(oldSize, moreItems.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         val view:View
         when (viewType) {
             DATA -> {
                 view = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false)
                 return NewsViewHolder(view)
             }
             DATE -> {
                 view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false)
                 return DateViewHolder(view)
             }
             else-> {
                 view = LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false)
                 return FooterViewHolder(view)
             }

         }
    }


    override fun getItemViewType(position: Int): Int {
        if (newsList.isEmpty()) {
            Log.d("TAG", "emptyList")
        }

        val item = newsList[position] // 这里的缩进应该与前面的代码一致
        return when (item) {
            is NewsItems.DateHeader -> DATE
            is NewsItems.StoryItem -> DATA
            else -> ITEM_FOOTER
        }
    }


    override fun onBindViewHolder(holder:RecyclerView.ViewHolder , position: Int) {


        if(holder is NewsViewHolder && position < newsList.size) {//newsList.size已经加一
            val news = ( newsList[position] as NewsItems.StoryItem).story
            holder.title.text = news.title
            holder.author.text = news.hint
            Glide.with(context).load(news.images[0]).into(holder.image)
            holder.itemView.setOnClickListener {
                onClick(news)
            }
        }
        if(holder is DateViewHolder && position < newsList.size) {
            val date = ( newsList[position] as NewsItems.DateHeader).date
            holder.date.text = date
        }
    }



    override fun getItemCount(): Int {
        return newsList.size
    }
}
