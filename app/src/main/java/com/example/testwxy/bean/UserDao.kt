package com.example.testwxy.bean

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/1 20:56
 */
@Entity(tableName = "login")
data class Login(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "is_login") val isLogin: Boolean = false//记住密码
)
@Dao
interface UserDao {
    @Insert
    suspend fun insert(login: Login)

    @Query("SELECT * FROM login WHERE username= :username AND password= :password")

    suspend fun getLogin(username: String, password: String): Login?

    @Query("UPDATE login SET is_login = :remeber WHERE username= :username")
    suspend fun rememberLogin(username: String, remeber: Boolean)


}