package com.example.testwxy.feature.home

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testwxy.R
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.data.model.NewsItems
import com.example.testwxy.data.model.Resource
import com.example.testwxy.data.model.buildMixedList
import com.example.testwxy.databinding.ActivityMainBinding
import com.example.testwxy.feature.adapter.NewsAdapter
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
    private val viewModel: MainViewModel by viewModels()

    private val prefs by lazy { getSharedPreferences("config", MODE_PRIVATE) }

    // 标记当前是什么模式
    private var isComposeMode = false

    // 使用日期追踪替代数字计数，解决日期跳跃问题
    private var oldestDateInList: String = ""
    private var isLoading = false

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isComposeMode = prefs.getBoolean("is_compose_mode", false)
        if (isComposeMode) {
            setContent {MainScreen(viewModel)  }
        } else {
            setContentView(binding.root)
            initView()
            initEvent()
            // 设置观察者
            setupObservers()

        }
        enableEdgeToEdge()
        viewModel.getNews()
    }


    @Composable
    fun MainScreen(viewModel: MainViewModel) {
        val newsResource by viewModel.newsResult.observeAsState()
        val recentResource by viewModel.recentNewsResult.observeAsState()
        val context = LocalContext.current

        LaunchedEffect(newsResource) {
            when (val res = newsResource) {
                is Resource.Success -> viewModel.handleInitialNews(res.data)
                is Resource.Error -> Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        LaunchedEffect(recentResource) {
            when (val res = recentResource) {
                is Resource.Success -> viewModel.handleHistoryNews(res.data)
                is Resource.Error -> {
                    Toast.makeText(context, "获取历史失败", Toast.LENGTH_SHORT).show()
                    viewModel.fullNewsList.removeAll { it is NewsItems.Footer }
                }

                else -> {}
            }
        }

        MainPage(
            newsItems = viewModel.fullNewsList,
            onStoryClick = { story ->
                NewsDetailActivity.start(context, story.url, story.date)
            },
            onBnnerClick = { topStory ->
                BannerDetailActivity.start(context, topStory.url)
            },
            onAvatarClick = {
                startActivity(Intent(this, PersonalActivity::class.java))
            }
            ,
            onLoadMore = {
                viewModel.loadMore()
            }
        )
    }

    private fun initView() {
        //初始化 NewsAdapter，传入 Banner 点击和普通新闻点击两个回调
        newsAdapter = NewsAdapter(
            onBannerClick = { topStory ->
                BannerDetailActivity.start(this, topStory.url)
            },
            onStoryClick = { story ->
                NewsDetailActivity.start(this, story.url, story.date)
            }
        )

        //平板适配逻辑
        val columnCount = resources.getInteger(R.integer.main_column_count)
        if (columnCount > 1) {
            val gridLayoutManager = GridLayoutManager(this, columnCount)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = newsAdapter.getItemViewType(position)
                    // Banner、日期标题、Footer 均需横跨全屏
                    return if (viewType == NewsAdapter.TYPE_STORY) 1 else columnCount
                }
            }
            binding.rvList.layoutManager = gridLayoutManager
        } else {
            binding.rvList.layoutManager = LinearLayoutManager(this)
        }
        binding.rvList.adapter = newsAdapter

        //下拉刷新
        binding.swipeRefreshLayout.apply {
            setProgressViewOffset(true, -100.dpToPx(), 40.dpToPx())
            val columnCount = resources.getInteger(R.integer.main_column_count)
            if (columnCount > 1) {
                setDistanceToTriggerSync(300.dpToPx())
            } else setDistanceToTriggerSync(400.dpToPx())
            setColorSchemeResources(R.color.littlebule)
            setOnRefreshListener {
                oldestDateInList = ""
                viewModel.getNews()
            }
        }

        binding.title.text = Tool.getDate()
        binding.title2.text = Tool.getTime()
        setupAvatar()
        setupScrollListener()
    }

    private fun setupObservers() {
        mLiveData.observe(this) { binding.menuIcon.setImageBitmap(it) }

        //历史新闻
        viewModel.recentNewsResult.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    oldestDateInList = resource.data.date

                    //这里人为添加了date参数，这在story的返回数据类并没有存在，为了迎合该项目的点击逻辑，这里人为添加date
                    resource.data.stories.forEach { story ->
                        story.date = resource.data.date
                    }

                    val currentItems = newsAdapter.currentList.toMutableList()
                    // 移除旧 Footer
                    val filteredItems = currentItems.filter { it !is NewsItems.Footer }
                    // 注意：加载历史时 buildMixedList 的 topStories 传 null，确保 Banner 只出现在最顶部
                    val nextBatch = buildMixedList(resource.data.stories, resource.data.date, null)

                    newsAdapter.submitList(filteredItems + nextBatch) {
                        isLoading = false
                        checkIfNeedMoreContent()
                    }
                }

                is Resource.Error -> {
                    isLoading = false
                    Toast.makeText(this, "加载历史内容失败", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

        //今日新闻 (首屏加载/刷新)
        viewModel.newsResult.observe(this) { resource ->
            binding.swipeRefreshLayout.isRefreshing = false
            when (resource) {
                is Resource.Success -> {
                    oldestDateInList = resource.data.date

                    //通过 buildMixedList 将 Banner 数据和今日新闻一起包装成列表
                    val updatedStories = Tool.updateStoriesWithNewsDate(resource.data)
                    val mixedList = buildMixedList(
                        updatedStories.stories,
                        resource.data.date,
                        resource.data.top_stories // 只有这里传入 Banner 数据
                    )

                    newsAdapter.submitList(mixedList)

                    //数据上屏后检查一次
                    binding.rvList.post { checkIfNeedMoreContent() }
                }

                is Resource.Error -> Toast.makeText(this, "获取新闻失败", Toast.LENGTH_SHORT).show()
                else -> {}
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
                if (dy > 0 && !isLoading) checkIfNeedMoreContent()
            }
        })
    }

    private fun checkIfNeedMoreContent() {
        if (isLoading || oldestDateInList.isEmpty()) return

        val lm = binding.rvList.layoutManager as? LinearLayoutManager ?: return
        val totalItemCount = lm.itemCount
        val lastVisibleItemPosition = lm.findLastVisibleItemPosition()

        val columnCount = resources.getInteger(R.integer.main_column_count)
        val threshold = if (columnCount > 1) 6 else 4

        if (totalItemCount > 0 && lastVisibleItemPosition >= totalItemCount - threshold) {
            onReachBottom()
        }
    }

    private fun onReachBottom() {
        if (isLoading) return
        isLoading = true
        Log.d("Pagination", "请求: oldestDateInList $oldestDateInList 之前的数据")
        viewModel.getRecentNews(oldestDateInList)
    }

    private fun initEvent() {
        binding.menuIcon.setOnClickListener {
            startActivity(Intent(this, PersonalActivity::class.java))
        }
    }

    private fun setupAvatar() {
        val base64 = sharedPreferences.getString("image", "")
        if (base64.isNullOrEmpty()) {
            binding.menuIcon.setImageBitmap(
                Tool.getCircularBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.tou
                    )
                )
            )
        } else {
            binding.menuIcon.setImageBitmap(Tool.getCircularBitmap(Tool.base64ToBitmap(base64)))
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

}