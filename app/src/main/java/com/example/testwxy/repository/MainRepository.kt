package com.example.testwxy.repository

import android.util.Log
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 15:50
 */
class MainRepository {
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("https://news-at.zhihu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsServie::class.java)
    }

   suspend fun getNews():ResultData<News>{
      return try{
          val response=retrofit.getNews()
          if(response.isSuccessful){
              response.body()?.let {
                  val body=it
                  Log.d("successNewsData",body.toString())
                  ResultData.Success(it)
              } ?: ResultData.Error(Exception("Body is null"))
              }
          else {
              Log.d("errorNewsData",response.errorBody().toString())
              ResultData.Error(Exception("error"))
          }

      }catch (e:Exception){
          ResultData.Error(e)
      }
   }
}
interface NewsServie{
    @GET("api/4/news/latest")
    suspend fun getNews():Response<News>

}