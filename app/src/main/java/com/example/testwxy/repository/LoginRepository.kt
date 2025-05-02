package com.example.testwxy.repository

import android.content.Context
import com.example.testwxy.bean.Login
import com.example.testwxy.bean.UserDao

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/1 21:45
 */
class LoginRepository(private val userDao: UserDao) {

    suspend fun register(login: Login)=userDao.insert(login)

    suspend fun login(username:String,password:String):Login?{
        return userDao.getLogin(username,password)
    }

     fun remeberLogin(username:String,password: String,context: Context){
         val store = context.getSharedPreferences("login", Context.MODE_PRIVATE)
         val editor = store.edit()
         editor.putString("username",username)
         editor.putString("password",password)
         editor.apply()
    }
    fun rember(context: Context,check:String){
        val store = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = store.edit()
        editor.putString("check",check)
        editor.apply()
    }

}