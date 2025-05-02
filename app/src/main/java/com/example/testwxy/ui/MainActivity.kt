package com.example.testwxy.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.testwxy.R
import com.example.testwxy.adapter.BannerAdapter
import com.example.testwxy.databinding.ActivityMainBinding
import com.example.testwxy.repository.ResultData
import com.example.testwxy.repository.TopStory
import com.example.testwxy.tool.Tool
import com.example.testwxy.viewmodel.MainViewModel
import kotlinx.coroutines.NonCancellable.isActive


val mLiveData: MutableLiveData<Bitmap> = MutableLiveData()

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager:ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var bannerList:List<TopStory>
    private lateinit var viewModel: MainViewModel
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        viewModel.newsResult.observe(this){
               result->
            when(result){
                is ResultData.Success->{

                    bannerList=result.data.top_stories

                    setupIndicators(bannerList.size)
                    setCurrentIndicator(0)

                    // 3. 监听页面切换，更新指示器状态
                    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            val realPosition = position % bannerList.size
                            setCurrentIndicator(realPosition)
                        }
                    })

                    viewPager.adapter = BannerAdapter(this, bannerList) { item ->

                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("url", item.url)
                        startActivity(intent)

                    }
                    // 设置初始位置为中间位置
                    val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % bannerList.size)
                    viewPager.setCurrentItem(startPosition, false)


                }
                is ResultData.Error->{
                    Toast.makeText(this,"错误返回数据为空",Toast.LENGTH_SHORT).show()

                }
            }

            }
        }
    private fun setupIndicators(count: Int) {
        indicatorLayout.removeAllViews()
        for (i in 0 until count) {
            val dot = ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.indicator_inactive))
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params
                scaleX = 0.5f // 初始缩小一点，未选中状态
                scaleY = 0.5f
            }
            indicatorLayout.addView(dot)
        }
    }


    private fun setCurrentIndicator(index: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val imageView = indicatorLayout.getChildAt(i) as ImageView

            val drawableId = if (i == index) R.drawable.indicator_active else R.drawable.indicator_inactive
            imageView.setImageDrawable(ContextCompat.getDrawable(this, drawableId))

            val targetScale = if (i==index) 1.2f else 0.8f

            // 动画：逐渐缩放
            imageView.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .setDuration(250)
                .start()
        }
    }


}


