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
  private var repository:LoginRepository
    val loginResult= MutableLiveData<Result<Login>>()
    val registerResult=MutableLiveData<Result<Unit>>()

    init{
        val userDao=AppDatabase.getDatabase(application).userDao()
         repository= LoginRepository(userDao)
    }
    fun login(username:String,password:String){
        viewModelScope.launch {
            try{
                val login=repository.login(username,password)

                if(login !=null){
                    loginResult.value=Result.success(login)
                }
                else{
                    loginResult.value=Result.failure(Exception("账号或者密码错误"))
                }

            }catch (e:Exception){
                loginResult.value=Result.failure(e)
            }
        }

    }
    fun register(username: String,password: String,context: Context){
        viewModelScope.launch {
            try{
                repository.register(Login(username=username, password = password))
                registerResult.value=Result.success(Unit)
                repository.remeberLogin(username,password,context)
            }catch (e:Exception){
                registerResult.value= Result.failure(e)
            }
        }
    }
    fun remeberLogin(context: Context,check:String){
          repository.rember(context,check)
    }
}