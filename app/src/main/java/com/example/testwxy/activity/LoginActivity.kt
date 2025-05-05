package com.example.testwxy.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.databinding.ActivityLoginBinding
import com.example.testwxy.ui.MainActivity
import com.example.testwxy.viewmodel.LoginViewModel
class LoginActivity : AppCompatActivity() {
private val binding: ActivityLoginBinding by lazy {
    ActivityLoginBinding.inflate(layoutInflater)
}
private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initEvent()
        setupObservers()
        check()
    }

    private fun check() {
        val store = getSharedPreferences("login", MODE_PRIVATE)
        if (store.getString("check", "")=="true") {
            binding.chLoginKeeppassword.isChecked=true
            binding.tietLoginUsername1.setText(store.getString("username", ""))
            binding.tietLoginPassword1.setText(store.getString("password", ""))
        }
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this){
            result-> result
                .onSuccess {  Intent(this, MainActivity::class.java).apply { startActivity(this) }
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show() }
                .onFailure { e-> Toast.makeText(this,"登录失败$e",Toast.LENGTH_SHORT).show() }
        }
    }

    fun initEvent() {
        binding.chLoginKeeppassword.setOnCheckedChangeListener  { buttonView, isChecked ->
            if (isChecked) {
                // 复选框被选中
                Toast.makeText(this, "保存密码", Toast.LENGTH_SHORT).show()
                viewModel.remeberLogin(this,"true")
            } else {
                // 复选框被取消选中
                Toast.makeText(this, "取消保存密码", Toast.LENGTH_SHORT).show()
                viewModel.remeberLogin(this,"false")
            }
        }

        binding.button1.setOnClickListener {
           viewModel.login(binding.tietLoginUsername1.text.toString(),binding.tietLoginPassword1.text.toString())
        }
        binding.tvLogin.setOnClickListener(
            {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        )
    }

}