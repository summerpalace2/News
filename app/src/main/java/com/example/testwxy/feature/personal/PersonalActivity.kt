package com.example.testwxy.feature.personal

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.R
import com.example.testwxy.databinding.ActivityPersonalBinding
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.feature.home.mLiveData


class PersonalActivity : AppCompatActivity() {
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private val prefs by lazy { getSharedPreferences("config", MODE_PRIVATE) }
    private val binding: ActivityPersonalBinding by lazy {
        ActivityPersonalBinding.inflate(layoutInflater)
    }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("login", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initView()
        initEvent()
    }

    private fun initView() {
        binding.usernames.setText("知乎用户")
        val original: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.tou)
        val circularBitmap = Tool.getCircularBitmap(original)
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
        updateSwitchStatusUI(prefs.getBoolean("is_compose_mode", false))

    }

    private fun initEvent() {
        binding.ivReturn.setOnClickListener { finish() }
        binding.nameButton.setOnClickListener { showImagePickerDialog() }
        // 注册拍照 Launcher
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
                bitmap?.let {
                    val circularBitmap = Tool.getCircularBitmap(it)
                    binding.imageView.setImageBitmap(circularBitmap)
                    mLiveData.value = circularBitmap
                    val base64String = Tool.bitmapToBase64(circularBitmap)
                    sharedPreferences.edit().putString("image", base64String).apply()
                }
            }

        // 注册相册 Launcher
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    val circularBitmap = Tool.getCircularBitmap(bitmap) // 转换为圆形头像
                    binding.imageView.setImageBitmap(circularBitmap)
                    mLiveData.value = circularBitmap
                    val base64String = Tool.bitmapToBase64(circularBitmap)
                    sharedPreferences.edit().putString("image", base64String).apply()
                }
            }


        binding.btnSwitchCompose.setOnClickListener {
            val currentMode = prefs.getBoolean("is_compose_mode", false)
            val targetMode = !currentMode // 取反

            toggleEngine(targetMode)

            updateSwitchStatusUI(targetMode)

            Toast.makeText(this, "引擎已切换，重启应用生效", Toast.LENGTH_SHORT).show()

        }
    }
    /**
     * 专门负责根据模式更新 UI 的方法
     */
    private fun updateSwitchStatusUI(isCompose: Boolean) {
        if (isCompose) {
            binding.tvCurrentStatus.text = "当前: Compose"
            binding.tvCurrentStatus.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            binding.tvCurrentStatus.text = "当前: XML View"
            binding.tvCurrentStatus.setTextColor(Color.parseColor("#1A73E8"))
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("拍照", "从相册选择")//获取对应的数字
        AlertDialog.Builder(this)
            .setTitle("选择头像来源")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> cameraLauncher.launch(null)
                    1 -> galleryLauncher.launch("image/*")//表示选择器会过滤图片文件（所有格式的图片）。
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    /**
     * 切换引擎的方法
     * @param targetMode true 为 Compose, false 为 XML
     */
    fun toggleEngine(targetMode: Boolean) {
        prefs.edit().putBoolean("is_compose_mode", targetMode).apply()
    }

}


