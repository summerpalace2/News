package com.example.testwxy.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.testwxy.R
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.data.model.NewsItems
import com.example.testwxy.data.model.Story
import com.example.testwxy.data.model.TopStory
import com.example.testwxy.feature.ViewPager2ConflictHelper

/**
 * description ： TODO:主页rv适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/5 17:25
 */

class NewsAdapter(
    private val onBannerClick: (TopStory) -> Unit,
    private val onStoryClick: (Story) -> Unit
) : ListAdapter<NewsItems, RecyclerView.ViewHolder>(NewsDiffCallback()) {

    companion object {
        const val TYPE_BANNER = 0
        const val TYPE_DATE = 1
        const val TYPE_STORY = 2
        const val TYPE_FOOTER = 3
    }

    private val handler = android.os.Handler(android.os.Looper.getMainLooper())

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewPager: ViewPager2 = itemView.findViewById(R.id.vp_banner)
        private val indicator: LinearLayout = itemView.findViewById(R.id.indicator)
        private var currentBannerAdapter: BannerAdapter? = null


        init {
            ViewPager2ConflictHelper.handleConflict(viewPager)
            // 配置平板特有的 Transformer
            val columnCount = itemView.resources.getInteger(R.integer.main_column_count)
            if (columnCount > 1) {
                viewPager.offscreenPageLimit = 3
                val margin = itemView.resources.getDimensionPixelOffset(R.dimen.banner_item_margin)
                viewPager.setPageTransformer(
                    androidx.viewpager2.widget.CompositePageTransformer().apply {
                        addTransformer(androidx.viewpager2.widget.MarginPageTransformer(margin))
                        addTransformer { page, position ->
                            val r = 1 - kotlin.math.abs(position)
                            page.scaleY = 0.85f + r * 0.15f
                        }
                    })
            }
        }

        fun bind(topStories: List<TopStory>) {

            if (viewPager.adapter == null) {
                currentBannerAdapter = BannerAdapter(topStories) { item ->
                    // 这里只处理中间图片的点击
                    onBannerClick(item)
                }
            }
            viewPager.adapter = currentBannerAdapter

            // 起始位置
            val startPos = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % topStories.size)
            viewPager.setCurrentItem(startPos, false)
            Tool.setupIndicators(topStories.size, indicator, itemView.context)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Tool.setCurrentIndicator(
                        position % topStories.size,
                        indicator,
                        itemView.context
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) stopScroll()
                    else if (state == ViewPager2.SCROLL_STATE_IDLE) startScroll()
                }
            })

            startScroll()
        }

        // 定时轮播任务
        private val scrollRunnable = object : Runnable {
            override fun run() {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                handler.postDelayed(this, 5000)
            }
        }

        fun startScroll() {
            handler.removeCallbacks(scrollRunnable)
            handler.postDelayed(scrollRunnable, 5000)
        }

        fun stopScroll() {
            handler.removeCallbacks(scrollRunnable)
        }
    }

    // 生命周期优化：当轮播图滑出屏幕时停止计时，滑入时开启
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is BannerViewHolder) holder.startScroll()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is BannerViewHolder) holder.stopScroll()
    }


    inner class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = itemView.findViewById(R.id.tv_News_title)
        private val author: TextView = itemView.findViewById(R.id.tv_News_author)
        private val image: ImageView = itemView.findViewById(R.id.tv_News_image)

        fun bind(item: Story) {
            title.text = item.title
            author.text = item.hint
            // 使用 ?. 确保 images 列表不为 null
            // 使用 getOrNull(0) 确保列表为空时不越界
            val imageUrl = item.images?.getOrNull(0)
            Glide.with(itemView.context)
                .load(imageUrl)
                .error(R.drawable.tou)       // 加载失败或 URL 为空时显示
                .fallback(R.drawable.tou)    // 当 model 为 null 时显示
                .into(image)
            itemView.setOnClickListener { onStoryClick(item) }
        }
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTv: TextView = itemView.findViewById(R.id.date)
        fun bind(date: String) {
            dateTv.text = date
        }
    }

    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NewsItems.BannerHeader -> TYPE_BANNER
            is NewsItems.DateHeader -> TYPE_DATE
            is NewsItems.StoryItem -> TYPE_STORY
            is NewsItems.Footer -> TYPE_FOOTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(
                inflater.inflate(
                    R.layout.item_header_banner,
                    parent,
                    false
                )
            )

            TYPE_DATE -> DateViewHolder(
                inflater.inflate(
                    R.layout.item_date,
                    parent,
                    false
                )
            )

            TYPE_STORY -> StoryViewHolder(
                inflater.inflate(
                    R.layout.item_rv,
                    parent,
                    false
                )
            )

            else -> FooterViewHolder(
                inflater.inflate(
                    R.layout.item_footer,
                    parent,
                    false
                )
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is BannerViewHolder -> holder.bind((item as NewsItems.BannerHeader).topStories)
            is StoryViewHolder -> holder.bind((item as NewsItems.StoryItem).story)
            is DateViewHolder -> holder.bind((item as NewsItems.DateHeader).date)
        }
    }
}


// 差分算法
class NewsDiffCallback : DiffUtil.ItemCallback<NewsItems>() {
    override fun areItemsTheSame(oldItem: NewsItems, newItem: NewsItems): Boolean {
        return when {
            oldItem is NewsItems.StoryItem && newItem is NewsItems.StoryItem -> oldItem.story.id == newItem.story.id
            oldItem is NewsItems.DateHeader && newItem is NewsItems.DateHeader -> oldItem.date == newItem.date
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: NewsItems, newItem: NewsItems): Boolean =
        oldItem == newItem
}