package com.example.testwxy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.example.testwxy.R
import com.example.testwxy.repository.Story

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/4 22:58
 */
class NewsUrladapter(
    private val context:Context,
    private var url: List<Story>
):RecyclerView.Adapter<NewsUrladapter.ViewHolder> (){
        inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            val web:WebView= itemView.findViewById(R.id.webviews)

        }

     fun updataData(news: List<Story>){
        val oldsize=url.size
        url=url+news
        notifyItemRangeInserted(oldsize,news.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_webview,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        url[position].url.let { holder.web.loadUrl(it) }
    }
    override fun getItemCount(): Int {
        return url.size
    }
    fun getItem(position: Int): Story? {
        return if (position in 0 until url.size) {
            url[position]
        } else {
            null
        }
    }
    }





