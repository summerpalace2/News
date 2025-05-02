package com.example.testwxy.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.R
import com.example.testwxy.databinding.ActivityPersonalBinding
import com.example.testwxy.tool.Tool

class PersonalActivity : AppCompatActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private val binding: ActivityPersonalBinding by lazy {
        ActivityPersonalBinding.inflate(layoutInflater)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        binding.usernames.setText(sharedPreferences.getString("username", ""))
        val original: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tou)
        val circularBitmap =Tool.getCircularBitmap(original)
        if (sharedPreferences.getString("image", "") == "") {
            val original: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tou)
            val circularBitmap = Tool.getCircularBitmap(original)
            binding.imageView.setImageBitmap(circularBitmap)
        } else {
            val base64String = sharedPreferences.getString("image", "")
            val bitmap = Tool.base64ToBitmap(base64String!!)
            val circularBitmap = Tool.getCircularBitmap(bitmap)
            binding.imageView.setImageBitmap(circularBitmap)
        }
    }

    private fun initEvent() {
        binding.tvReturn.setOnClickListener{finish()}
       binding.nameButton.setOnClickListener{ showImagePickerDialog() }
        // 注册拍照 Launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                val circularBitmap = Tool.getCircularBitmap(it)
                binding.imageView.setImageBitmap(circularBitmap)
                mLiveData.value = circularBitmap
                val base64String = Tool.bitmapToBase64(circularBitmap)
                sharedPreferences.edit().putString("image", base64String).apply()
            }
        }

        // 注册相册 Launcher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                val circularBitmap = Tool.getCircularBitmap(bitmap) // 转换为圆形头像
                binding.imageView.setImageBitmap(circularBitmap)
                mLiveData.value = circularBitmap
                val base64String = Tool.bitmapToBase64(circularBitmap)
                sharedPreferences.edit().putString("image", base64String).apply()
            }
        }
    }
    private fun showImagePickerDialog() {
        val options = arrayOf("拍照", "从相册选择")
        AlertDialog.Builder(this)
            .setTitle("选择头像来源")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> cameraLauncher.launch(null)
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    }


