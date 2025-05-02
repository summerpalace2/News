package com.example.testwxy.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testwxy.databinding.ActivityRegisterBinding
import com.example.testwxy.viewmodel.LoginViewModel

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy{ActivityRegisterBinding.inflate(layoutInflater)}
    private val viewmodel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initEvent()
        setupObservers()
    }

    private fun setupObservers() {
        viewmodel.registerResult.observe(this){
            result ->{

                result.onSuccess{
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show() }

            result.onFailure { e->Toast.makeText(this,"注册失败${e.message}",Toast.LENGTH_SHORT).show() }
        }
        }
    }

    private fun initEvent() {
        binding.Button2.setOnClickListener{
           if(binding.etRegPassword11.text.toString()==binding.etRegPassword22.text.toString()){
               viewmodel.register(binding.etRegUsername1.text.toString(),binding.etRegPassword11.text.toString(),this)
               finish()
           }
            else{
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show()
           }
        }
    }

}