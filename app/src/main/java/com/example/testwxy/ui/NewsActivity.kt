package com.example.testwxy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.testwxy.adapter.NewsUrladapter

import com.example.testwxy.databinding.ActivityNewsBinding
import com.example.testwxy.repository.CommentnumberResult
import com.example.testwxy.repository.ResultData
import com.example.testwxy.repository.ResultRecentData
import com.example.testwxy.repository.ResultUrlRecentData
import com.example.testwxy.tool.Tool
import com.example.testwxy.viewmodel.MainViewModel


class NewsActivity : AppCompatActivity() {
    private var id: Int?=null
    private var url: String?=null
    private var number=0
    private lateinit var commentAdapter: NewsUrladapter
    private lateinit var viewpager:ViewPager2
    private var previousPosition = 0
    private val viewModel: MainViewModel by lazy{
        MainViewModel(application)
    }
    private val binding: ActivityNewsBinding by lazy {
        ActivityNewsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initView()
        initEvent()
        observe()

        commentAdapter = NewsUrladapter(this, listOf()) // 初始化时空数据，避免空指针错误
        viewpager.adapter = commentAdapter // 设置 adapter

    }

    private fun initEvent() {
        binding.tvReturn.setOnClickListener {
            finish()
        }
        binding.thinkimage.setOnClickListener{
            intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)

        }
        binding.share.setOnClickListener {
            val errorUrl = "https://example.com/help/comment-error?id=$id"

            val builder = android.app.AlertDialog.Builder(this)
                .setTitle("分享")
                .setMessage("你可以分享以下链接给他人：\n\n$errorUrl")
                .setPositiveButton("分享链接") { dialog, _ ->
                    val shareIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, errorUrl)
                        type = "text/plain"
                    }
                    startActivity(android.content.Intent.createChooser(shareIntent, "分享到"))
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.show()

        }
    }

    private fun initView() {
        val date=intent.getStringExtra("date")?:" "
        viewpager=binding.webViewviewpager
        listener(date)
        getOnceListUrl(date)

    }

    private fun listener(date: String) {
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val currentDate = Tool.getDateNews()
                Log.d("datedate", "Today: $currentDate, Init Date: $date")

                // 用户向右滑动（向未来滑动）：
                if (position > previousPosition) {
                    Log.d("ScrollDirection", "→ 向右滑动")
                    if (position == commentAdapter.itemCount - 1) {
                        number++
                        getTwiceListUrl(Tool.getPreviousDays(date, number))
                    }
                }
                previousPosition = position

                // 根据当前的 position 获取对应的新闻并加载评论
                val currentStory = commentAdapter.getItem(position)  // 获取当前新闻
                currentStory?.let {
                    getcomment(it.id) // 获取评论
                    id=it.id//把id传递到下一个activity
                    url=it.url
                }

            }
        })
    }

    private fun getcomment(id:Int){
       viewModel.getnumberComment(id)
    }

    private fun getOnceListUrl(date:String){
        Log.d("dateNews",date)
        if(date== Tool.getDateNews()){
            viewModel.getNews()
        }
        else{
            viewModel.getRecentNews(Tool.getNextDay(date))
        }

    }
    private fun getTwiceListUrl(date:String){
        Log.d("dateNews",date)
        viewModel.getRecentNewsUrl(Tool.getNextDay(date))
    }

    private fun observe(){
        viewModel.newsResult.observe(this){
            result ->
                when (result) {
                    is ResultData.Success -> {
                        Log.d("NewsActivity", "news: $result")
                        val news = result.data
                        commentAdapter.updataData(news.stories)
                        for(i in 0 until news.stories.size){
                            if(news.stories[i].url == (intent.getStringExtra("url") ?: "")){
                                viewpager.setCurrentItem(i, true)
                            }
                        }
                    }

                    is ResultData.Error -> {
                        Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show()

                    }
                }

        }
        viewModel.recentNewsResult.observe(this){
            result ->
                when (result) {
                    is ResultRecentData.Success -> {
                        Log.d("NewsActivity", "news: $result")
                        val news = result.data
                        commentAdapter.updataData(news.stories)
                        viewpager.setCurrentItem(1, true)
                    }

                    is ResultRecentData.Error -> {
                        Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show()

                   }
                }
        }

        viewModel.resulturlRecentNewsResult.observe(this){
            result ->
                when (result) {
                    is ResultUrlRecentData.Success -> {
                        Log.d("RecentData", "news: $result")
                        val news = result.data
                        commentAdapter.updataData(news.stories)
                    }
                    is ResultUrlRecentData.Error -> {
                        Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        viewModel.commentnumberResult.observe(this){
            result ->
                when (result) {
                    is CommentnumberResult.Success -> {
                        Log.d("commentData", "news: $result")
                        val comment = result.data
                        binding.thinknumber.text = "评论数：${comment.comments}"
                        binding.likenumber.text = "喜欢数：${comment.popularity}"

                        Log.d("comment",comment.toString())
                    }
                    is CommentnumberResult.Error -> {
                        Toast.makeText(this, "返回错误", Toast.LENGTH_SHORT).show()
                    }
                }
        }



    }


}