package com.example.testwxy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testwxy.bean.AppDatabase
import com.example.testwxy.bean.Login
import com.example.testwxy.repository.LoginRepository
import kotlinx.coroutines.launch

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/1 21:10
 */
class LoginViewModel(application: Application):AndroidViewModel(application) {
    private var repository: LoginRepository
    val loginResult = MutableLiveData<Result<Login>>()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = LoginRepository(userDao)
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {//启动协程
            try {
                val login = repository.login(username, password)

                if (login != null) {
                    loginResult.value = Result.success(login)
                } else {
                    loginResult.value = Result.failure(Exception("账号或者密码错误"))
                }

            } catch (e: Exception) {
                loginResult.value = Result.failure(e)
            }
        }

    }

    fun register(username: String, password: String, context: Context) {
        viewModelScope.launch {

            repository.register(Login(username = username, password = password))//存储数据
            repository.remeberLogin(username, password, context)

        }
    }
    fun remeberLogin(context: Context, check: String) {
        repository.rember(context, check)//记住密码
    }
}