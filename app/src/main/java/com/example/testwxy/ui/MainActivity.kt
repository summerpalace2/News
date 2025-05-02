package com.example.testwxy.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.example.testwxy.R
import com.example.testwxy.databinding.ActivityLoginBinding
import com.example.testwxy.databinding.ActivityMainBinding
import com.example.testwxy.tool.Tool
 val mLiveData: MutableLiveData<Bitmap> = MutableLiveData()

class MainActivity : AppCompatActivity() {
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
        mLiveData.observe(this) {
            binding.menuIcon.setImageBitmap(it)

        }
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
    }
