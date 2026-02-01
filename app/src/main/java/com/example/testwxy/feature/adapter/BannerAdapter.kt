package com.example.testwxy.feature.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.data.model.TopStory

/**
 * description ： TODO:轮播图rv适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 17:41
 */

class BannerAdapter(
    private val bannerList: List<TopStory>,
    private val onClick: (TopStory) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_banner)
        val title: TextView = itemView.findViewById(R.id.tv_banner)
        val author: TextView = itemView.findViewById(R.id.tv_banner_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (bannerList.isEmpty()) return

        // 用于数据绑定的真实索引
        val realPosition = position % bannerList.size
        val item = bannerList.get(realPosition)

        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.tou)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.title.text = item.title
        holder.author.text = item.hint
        holder.itemView.setBackgroundColor(Color.BLACK)
    }

    override fun getItemCount() = if (bannerList.isEmpty()) 0 else Int.MAX_VALUE
}
