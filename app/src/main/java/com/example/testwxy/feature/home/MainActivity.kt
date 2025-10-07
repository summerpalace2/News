package com.example.testwxy.feature.home

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.testwxy.R
import com.example.testwxy.feature.adapter.BannerAdapter
import com.example.testwxy.feature.adapter.NewsAdapter
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.data.model.*
import com.example.testwxy.databinding.ActivityMainBinding
import com.example.testwxy.feature.news.BannerDetailActivity
import com.example.testwxy.feature.news.MainViewModel
import com.example.testwxy.feature.news.NewsDetailActivity
import com.example.testwxy.feature.personal.PersonalActivity

/**
 * description ： TODO:主显示页
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/1 14:28
 */
// 全局 LiveData 用于头像跨页面同步
val mLiveData: MutableLiveData<Bitmap> = MutableLiveData()

class MainActivity : AppCompatActivity() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var bannerList: List<TopStory>
    private lateinit var viewModel: MainViewModel
    // 分页标识：0 代表今日，1 代表昨天...
    private var number = 0

    // 状态锁：防止重复触发网络请求
    private var isLoading = false

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", MODE_PRIVATE)
    }

    // --- 轮播图自动滚动逻辑 ---
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (::bannerList.isInitialized && bannerList.isNotEmpty()) {
                val current = viewPager.currentItem
                viewPager.setCurrentItem(current + 1, true)
                handler.postDelayed(this, 3000) // 3秒切换一次
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        initView()
        initEvent()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNews() // 初始加载今日新闻

        setupObservers()

    }
    private fun initView() {
        viewPager = binding.vpBanner
        indicatorLayout = binding.indicator

        // 1. 配置下拉刷新 (SwipeRefreshLayout)
        val startPosition = -50.dpToPx() // 初始隐藏在更上方
        val endPosition = 30.dpToPx()    // 最终停留在靠上的位置（比如距离顶部 30dp）

        binding.swipeRefreshLayout.setProgressViewOffset(true, startPosition, endPosition)
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.littlebule)
        binding.swipeRefreshLayout.setOnRefreshListener {
            number = 0 // 重置分页
            stopAutoScroll()
            viewModel.getNews() // 刷新首页
        }

        // 2. 初始化 RecyclerView
        newsAdapter = NewsAdapter { news ->
            startActivity(Intent(this, NewsDetailActivity::class.java).apply {
                putExtra("url", news.url)
                putExtra("date", news.date)
            })
        }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = newsAdapter

        // 3. 设置静态文本
        binding.title.text = Tool.getDate()
        binding.title2.text = Tool.getTime()

        setupAvatar()

        // 4. 设置增强型滚动监听
        setupScrollListener()
    }
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun initEvent() {
        binding.menuIcon.setOnClickListener {
            startActivity(Intent(this, PersonalActivity::class.java))
        }
    }

    /**
     * 数据观察者核心逻辑
     */
    private fun setupObservers() {
        mLiveData.observe(this) { binding.menuIcon.setImageBitmap(it) }

        // 观察：历史新闻（触底加载追加）
        viewModel.recentNewsResult.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val updatedStories = Tool.updateStoriesWithNewsDate(resource.data)

                    // 1. 获取当前列表的快照（注意 currentList 是只读的，需要转成 Mutable）
                    val currentItems = newsAdapter.currentList.toMutableList()

                    // 2. 移除旧列表末尾的 Footer 占位符
                    val filteredItems = currentItems.filter { it !is NewsItems.Footer }

                    // 3. 构建新一批数据并合并（buildMixedList 内部会带入新的 Footer）
                    val nextBatch = buildMixedList(updatedStories.stories, resource.data.date)
                    val newList = filteredItems + nextBatch

                    // 4. 通过 submitList 提交，ListAdapter 会在后台对比差异并执行增量刷新
                    newsAdapter.submitList(newList) {
                        isLoading = false // 数据渲染完成后释放锁
                    }
                }
                is Resource.Error -> {
                    isLoading = false
                    Toast.makeText(this, "加载历史新闻失败", Toast.LENGTH_SHORT).show()
                }

                Resource.Loading -> {}//加载状态这里不做处理
            }
        }

        // 今日新闻（首屏加载/刷新）
        viewModel.newsResult.observe(this) { resource ->
            // 关闭下拉动画的逻辑移至最前，确保无论成败都停止转圈
            binding.swipeRefreshLayout.isRefreshing = false


            when (resource) {
                is Resource.Success -> {

                    // --- 轮播图(Banner) 初始化 ---
                    bannerList = resource.data.top_stories ?: emptyList()
                    if (bannerList.isNotEmpty()) {
                        Tool.setupIndicators(bannerList.size, binding.indicator, this)
                        viewPager.adapter = BannerAdapter(this, bannerList) { item ->
                            startActivity(Intent(this, BannerDetailActivity::class.java).apply {
                                putExtra("url", item.url)
                            })
                        }
                        // 无限循环位置设置
                        val startPos = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerList.size)
                        viewPager.setCurrentItem(startPos, false)

                        // 监听滑动以控制自动轮播
                        viewPager.registerOnPageChangeCallback(object :
                            ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                val realPos = position % bannerList.size
                                Tool.setCurrentIndicator(realPos, binding.indicator, this@MainActivity)
                            }

                            override fun onPageScrollStateChanged(state: Int) {
                                // 用户手动拖拽时停止轮播，静止后恢复
                                if (state == ViewPager2.SCROLL_STATE_DRAGGING) stopAutoScroll()
                                else if (state == ViewPager2.SCROLL_STATE_IDLE) startAutoScroll()
                            }
                        })
                        startAutoScroll()
                    }

                    // --- 列表数据初始化 ---
                    val updatedStories = Tool.updateStoriesWithNewsDate(resource.data)
                    // 首屏加载直接提交整个构建好的混合列表
                    newsAdapter.submitList(buildMixedList(updatedStories.stories, resource.data.date))

                    // 首屏填充检查
                    binding.rvList.post {
                        checkIfNeedMoreContent()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, "获取今日新闻失败", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
            }
        }
    }

    /**
     * 增强型触底检测逻辑
     */
    private fun setupScrollListener() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                // 仅在向下滑动且未处于加载状态时检查
                if (dy > 0 && !isLoading) {
                    checkIfNeedMoreContent()
                }
            }
        })
    }

    /**
     * 提前加载逻辑，防止空白页
     */
    private fun checkIfNeedMoreContent() {
        val lm = binding.rvList.layoutManager as LinearLayoutManager
        val lastVisible = lm.findLastVisibleItemPosition()
        //  ListAdapter 内部条目总数
        val total = newsAdapter.itemCount

        // 如果最后 2 条 item（包含 Footer）已经露脸，就加载更多
        if (lastVisible >= total - 2 && total > 0) {
            onReachBottom()
        }
    }
//触底就刷新数据
    private fun onReachBottom() {
        if (isLoading) return
        isLoading = true
        number++
        val targetDate = Tool.getDateNDaysAgo(number.toLong())
        Log.d("Pagination", "请求日期: $targetDate")
        viewModel.getRecentNews(targetDate)
    }

    // --- 自动轮播管理 ---
    private fun startAutoScroll() {
        stopAutoScroll()
        handler.postDelayed(autoScrollRunnable, 5000)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    private fun setupAvatar() {
        val base64 = sharedPreferences.getString("image", "")
        if (base64.isNullOrEmpty()) {
            val original = BitmapFactory.decodeResource(resources, R.drawable.tou)
            binding.menuIcon.setImageBitmap(Tool.getCircularBitmap(original))
        } else {
            val bitmap = Tool.base64ToBitmap(base64)
            binding.menuIcon.setImageBitmap(Tool.getCircularBitmap(bitmap))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll()
        handler.removeCallbacksAndMessages(null)
    }
}