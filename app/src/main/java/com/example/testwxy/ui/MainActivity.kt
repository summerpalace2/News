package com.example.testwxy.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.testwxy.R
import com.example.testwxy.adapter.BannerAdapter
import com.example.testwxy.adapter.NewsAdapter
import com.example.testwxy.databinding.ActivityMainBinding
import com.example.testwxy.repository.ResultData
import com.example.testwxy.repository.ResultRecentData
import com.example.testwxy.repository.Story
import com.example.testwxy.repository.TopStory
import com.example.testwxy.repository.buildMixedList
import com.example.testwxy.tool.Tool
import com.example.testwxy.tool.Tool.updateStoriesWithNewsDate
import com.example.testwxy.viewmodel.MainViewModel




val mLiveData: MutableLiveData<Bitmap> = MutableLiveData()
class MainActivity : AppCompatActivity() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewPager:ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var bannerList:List<TopStory>
    private lateinit var viewModel: MainViewModel
    private var number=-1
    private var isLoading = false
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initView()
        initEvent()
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getNews()
        setupObservers()

    }

    private fun initEvent() {
        binding.menuIcon.setOnClickListener {
            Intent().apply {
                setClass(this@MainActivity, PersonalActivity::class.java)
                startActivity(this)
            }
        }
    }

    private fun initView() {
        viewPager = binding.vpBanner
        indicatorLayout = binding.indicator
        // 初始化 RecyclerView Adapter（可先用空数据）
        // 禁用 RecyclerView 自身滑动，由 NestedScrollView 接管
        viewPager.isNestedScrollingEnabled = false

//        ✅ 初始化 NewsAdapter 使用空数据
        newsAdapter = NewsAdapter(this, emptyList()) { news ->
            val intent = Intent(this, NewsActivity::class.java)
            intent.putExtra("url", news.url)
            intent.putExtra("date", news.date)
            startActivity(intent)
        }//2. 你需要先设置 adapter，再异步加载数据
        //RecyclerView.adapter 一般在 onCreate() 里就设置好。
        //获取数据是异步的（比如 ViewModel + LiveData）
       // Adapter 不能等数据到了再创建，否则会导致 RecyclerView 没内容、无法初始化。
        //所以你先用空数据创建 Adapter，后续再通过 updateData() 或 appendData() 动态更新
        binding.rvList.layoutManager = LinearLayoutManager(this)

        binding.rvList.adapter = newsAdapter
        binding.title.setText(Tool.getDate())
        binding.title2.setText(Tool.getTime())
        if (sharedPreferences.getString("image", "") == "") {
            val original: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tou)
            val circularBitmap = Tool.getCircularBitmap(original)
            binding.menuIcon.setImageBitmap(circularBitmap)
        } else {
            val base64String = sharedPreferences.getString("image", "")
            val bitmap = Tool.base64ToBitmap(base64String!!)
            val circularBitmap = Tool.getCircularBitmap(bitmap)
            binding.menuIcon.setImageBitmap(circularBitmap)
        }
    }
    private fun setupObservers(){
        mLiveData.observe(this) {
            binding.menuIcon.setImageBitmap(it)
        }
        viewModel.recentNewsResult.observe(this){
                result->
            when(result){
                is ResultRecentData.Success ->{
                    val updatedNews = updateStoriesWithNewsDate(result.data)
                    Log.d("updateNews", updatedNews.toString())
                    loadMoreItems(updatedNews.stories,result.data.date)

                }
                is ResultRecentData.Error->{
                    Toast.makeText(this,"访问之前的新闻失败",Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.newsResult.observe(this){
                result->
            when(result){
                is ResultData.Success->{

                    bannerList= result.data.top_stories!!


                    Tool.setupIndicators(bannerList.size, indicatorLayout,this)
                    Tool.setCurrentIndicator(0, indicatorLayout,this)

                    // 3. 监听页面切换，更新指示器状态
                    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            val realPosition = position % bannerList.size
                            Tool.setCurrentIndicator(realPosition,  indicatorLayout, this@MainActivity)
                        }
                    })

                    viewPager.adapter = BannerAdapter(this, bannerList) { item ->

                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("url", item.url)
                        startActivity(intent)

                    }//lambda表达式,回调事件   解耦合：Adapter 不需要知道点击后该做什么，由调用者（如 Activity）决定。
                    //灵活性高：你可以在不同地方以不同方式处理点击事件，复用 Adapter。
                    //代码更简洁：相比传统接口回调，这种 lambda 写法更清爽。


                    // 设置初始位置为中间位置
                    val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerList.size)
                    viewPager.setCurrentItem(startPosition, false)

                    //更新数据
                    val updatedNews = updateStoriesWithNewsDate(result.data)
                    Log.d("updateNews", updatedNews.toString())
                    newsAdapter.updateData(buildMixedList(updatedNews.stories, result.data.date))
                    observeRecycleview()


                    binding.scrollView.setOnScrollChangeListener { v: View, _, scrollY, _, oldScrollY ->
                        val view = v as NestedScrollView
                        // 到达底部
                        if (view.getChildAt(view.childCount - 1).bottom <= (view.height + scrollY)) {
                            if (!isLoading) {
                                onReachBottom()
                            }
                        }
                    }


                }
                is ResultData.Error->{
                    Toast.makeText(this,"错误返回数据为空",Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
    private fun observeRecycleview() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val layoutManager = rv.layoutManager as? LinearLayoutManager ?: return

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                // 到达底部判断条件
                if (visibleItemCount + lastVisibleItem >= totalItemCount && dy > 0) {
                    //“到底了”
                    onReachBottom()
                }
            }
        })
    }

    private fun onReachBottom() {
        if (isLoading) return
        isLoading = true
        number++
        Log.d("numberdate",number.toString())
        val date = Tool.getDateNDaysAgo(number.toLong())
        Log.d("becomedate",date)
        // 👇 在这里触发“到底了”的操作，比如加载更多
        viewModel.getRecentNews(date)

    }
    fun loadMoreItems(newItems: List<Story>,data:String) {
        // 假设延迟2秒加载
        Handler(Looper.getMainLooper()).postDelayed({
            // 加载完成，更新列表

            newsAdapter.appendData(buildMixedList(newItems,data))

            isLoading = false
        }, 1000)
    }

}


