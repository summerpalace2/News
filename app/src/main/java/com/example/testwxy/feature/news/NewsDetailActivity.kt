package com.example.testwxy.feature.news

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.data.model.Resource
import com.example.testwxy.data.model.Story
import com.example.testwxy.databinding.ActivityNewsBinding
import com.example.testwxy.feature.comment.CommentDetailActivity

/**
 * description ： TODO:新闻页 左右无限滑动版本
 * author : summer_palace2
 * date : 2026/1/25 11:31
 */
class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val stories = mutableListOf<Story>()

    private var currentIndex = 0
    private var currentId: Int? = null
    private var isLoading = false
    private var isAddingToTop = false

    // 记录服务器目前最新的日期，防止服务器数据更新延误造成bug
    private var serverLatestDate: String = ""
    private var oldestDate: String = "" // 列表中最旧日期
    private var newestDate: String = "" // 列表中最新日期

    private val viewModel: MainViewModel by lazy { MainViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val initDate = intent.getStringExtra("date") ?: Tool.getDateNews()
        oldestDate = initDate.toString()
        newestDate = initDate.toString()

        initWebView()
        initEvent()
        observe()

        // 数据初始化
        requestNewsByDate(initDate.toString())
    }

    private fun initWebView() {
        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.setSupportZoom(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        binding.webView.onSwipeLeft = { loadNextStory() }
        binding.webView.onSwipeRight = { loadPreviousStory() }
    }

    /**
     * 加载指定索引的新闻并处理预加载逻辑
     */
    private fun loadStory(index: Int) {
        if (index < 0 || index >= stories.size) return

        val story = stories[index]
        currentIndex = index
        currentId = story.id

        binding.webView.loadUrl(story.url)
        viewModel.getCommentInfo(story.id)

        // --- 预加载逻辑 ---
        // 防止从一天的新闻跳转到其他天的新闻引起的网络请求卡顿，给用户更好的滑动体验

        //  如果滑动到最后2篇：加载更早日期的内容
        if (index >= stories.size - 2 && !isLoading) {
            isAddingToTop = false
            val targetDate = Tool.getPreviousDay(oldestDate) // 目标是 oldestDate 的前一天
            Log.d("Paging", "触发加载更旧内容，目标日期: $targetDate")
            requestNewsByDate(targetDate)
        }

        // 2. 如果滑动到最前2篇：加载更晚日期 (更新)
        if (index <= 1 && !isLoading && newestDate != Tool.getDateNews()) {
            isAddingToTop = true
            val targetDate = Tool.getNextDay(newestDate) // 目标是 newestDate 的后一天
            Log.d("Paging", "触发加载更新内容，目标日期: $targetDate")
            requestNewsByDate(targetDate)
        }
    }

    /**
     * 封装请求逻辑，处理知乎 API 的日期偏移
     * @param targetDate 我们想要获取新闻的那一天
     * @param isFuture 是否是向上（未来）请求
     */
    private fun requestNewsByDate(targetDate: String) {
        isLoading = true
        if (targetDate == Tool.getDateNews()) {
            // 如果目标是今天，直接用今日接口，它不遵循 before 规则
            viewModel.getNews()
        } else {
            // 【修正逻辑】：知乎 before/{date} 返回的是 {date} 的前一天
            // 1. 如果想看 18号，参数应传 19号 (即 oldestDate)
            // 2. 如果想看 20号，参数应传 21号 (即 newestDate + 2)
            val apiParam = Tool.getNextDay(targetDate)

            Log.d("API_PARAM", "目标:$targetDate, 实际发送参数:before/$apiParam")
            viewModel.getRecentNews(apiParam)
        }
    }

    private fun observe() {
        // 今日新闻 (latest)
        viewModel.newsResult.observe(this) { resource ->
            if (resource !is Resource.Loading) isLoading = false
            when (resource) {
                is Resource.Success -> {
                    if (stories.isEmpty()) {
                        handleInitialData(resource.data.stories, resource.data.date)
                    } else if (isAddingToTop) {
                        handleHeadInsert(resource.data.stories, resource.data.date)
                    }
                }

                is Resource.Error -> Log.e("Detail", "Error: ${resource.exception.message}")
                else -> {}
            }
        }

        // 历史新闻 (before/date)
        viewModel.recentNewsResult.observe(this) { resource ->
            if (resource !is Resource.Loading) isLoading = false
            when (resource) {
                is Resource.Success -> {
                    val newStories = resource.data.stories
                    val dateOfData = resource.data.date

                    if (stories.isEmpty()) {
                        handleInitialData(newStories, dateOfData)
                    } else {
                        if (isAddingToTop) {
                            handleHeadInsert(newStories, dateOfData)
                        } else {
                            // 往尾部加 (过去内容)
                            stories.addAll(newStories)
                            oldestDate = dateOfData
                        }
                    }
                }

                is Resource.Error -> Log.e("Detail", "Error: ${resource.exception.message}")
                else -> {}
            }
        }

        viewModel.commentInfoResult.observe(this) { resource ->
            if (resource is Resource.Success) {
                binding.thinknumber.text = resource.data.comments.toString()
                binding.likenumber.text = resource.data.popularity.toString()
            }
        }
    }

    /**
     * 核心逻辑：头部插入数据并修正索引，防止视觉跳拽
     */
    private fun handleHeadInsert(newStories: List<Story>, date: String) {
        // 如果返回的日期并不比当前更新，拒绝插入防止死循环
        if (date <= newestDate && stories.isNotEmpty()) return

        stories.addAll(0, newStories)
        currentIndex += newStories.size // 索引后移，保证用户还在看当前这篇
        newestDate = date
    }

    private fun handleInitialData(newStories: List<Story>, date: String) {
        stories.clear()
        stories.addAll(newStories)
        oldestDate = date
        newestDate = date
        val targetUrl = intent.getStringExtra("url") ?: ""
        val startIdx = stories.indexOfFirst { it.url == targetUrl }
        loadStory(if (startIdx >= 0) startIdx else 0)
    }

    // -------------------- 导航方法 --------------------

    private fun loadNextStory() {
        if (currentIndex < stories.size - 1) {
            loadStory(currentIndex + 1)
        } else {
            // 到达末尾：主动检查并触发加载更旧的内容
            if (!isLoading) {
                val targetDate = Tool.getPreviousDay(oldestDate)
                requestNewsByDate(targetDate)
                Toast.makeText(this, "正在加载更早的内容...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "内容加载中，请稍后...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPreviousStory() {
        if (currentIndex > 0) {
            loadStory(currentIndex - 1)
        } else {
            // 如果当前日期已经达到了服务器已知的最新日期，显示最新
            if (newestDate >= serverLatestDate || newestDate == Tool.getDateNews()) {
                Toast.makeText(this, "已经是最新了", Toast.LENGTH_SHORT).show()
            } else {
                // 只有确实落后于已知日期时才刷新
                requestNewsByDate(Tool.getNextDay(newestDate))
            }
        }
    }

    private fun initEvent() {
        binding.tvReturn.setOnClickListener { finish() }
        binding.llComment.setOnClickListener {
            startActivity(Intent(this, CommentDetailActivity::class.java).apply {
                putExtra("id", currentId)
            })
        }
        binding.share.setOnClickListener {
            val shareUrl = "https://daily.zhihu.com/story/$currentId"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareUrl)
            }
            startActivity(Intent.createChooser(intent, "分享至"))
        }
    }
}