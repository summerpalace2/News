package com.example.testwxy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.repository.News
import com.example.testwxy.repository.TopStory

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 17:41
 */

class BannerAdapter(
    private val context: Context,
    private val bannerList: List<TopStory>,
    private val onClick: (TopStory) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_banner)
        val title: TextView = itemView.findViewById(R.id.tv_banner)
        val adutor: TextView = itemView.findViewById(R.id.tv_banner_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % bannerList.size
        val item = bannerList[realPosition]
        Glide.with(context).load(item.image).into(holder.image)
        holder.image.setOnClickListener { onClick(item) }
        holder.title.setText(item.title)
        holder.adutor.setText(item.hint)
    }

    override fun getItemCount() = Int.MAX_VALUE//设置最大数实现无限循环
}
