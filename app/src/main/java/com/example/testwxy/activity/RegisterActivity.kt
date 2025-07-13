package com.example.testwxy.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
        initEvent()

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