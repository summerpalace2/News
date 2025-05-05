package com.example.testwxy.repository

import android.util.Log
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    suspend fun getRecentNews(date: String):ResultRecentData<News>{
        return try{
            val response=retrofit.getRecentNews(date)
            if(response.isSuccessful){
                response.body()?.let {
                    val body=it
                    Log.d("successNewsData",body.toString())
                    ResultRecentData.Success(it)
                } ?: ResultRecentData.Error(Exception("Body is null"))
            }
            else {
                Log.d("errorNewsData",response.errorBody().toString())
                ResultRecentData.Error(Exception("error"))
            }

        }catch (e:Exception){
            ResultRecentData.Error(e)
        }
    }


    suspend fun getRecentNewsUrl(date: String):ResultUrlRecentData<News>{
        return try{
            val response=retrofit.getRecentUrlNews(date)
            if(response.isSuccessful){
                response.body()?.let {
                    val body=it
                    Log.d("successNewsData",body.toString())
                    ResultUrlRecentData.Success(it)
                } ?:ResultUrlRecentData.Error(Exception("Body is null"))
            }
            else {
                Log.d("errorNewsData",response.errorBody().toString())
                ResultUrlRecentData.Error(Exception("Error:  ${response.code()}"))
            }
        }catch (e:Exception){
            ResultUrlRecentData.Error(e)
        }
    }
    suspend fun getCommentnumber(id:Int):CommentnumberResult<Commentnumber>{
        return try{
            val response=retrofit.getCommentnumber(id)
            if(response.isSuccessful){
                response.body()?.let {
                    val body=it
                    Log.d("successCommentData",body.toString())
                    CommentnumberResult.Success(it)
                } ?:CommentnumberResult.Error(Exception("Body is null"))
            }
            else {
                Log.d("errorCommentData",response.errorBody().toString())
                CommentnumberResult.Error(Exception("Error:  ${response.code()}"))

            }
        }catch (e:Exception){
            CommentnumberResult.Error(e)
        }
    }
    suspend fun getComment(id:Int):CommentsResult<Comments>{
        return try{
            val response=retrofit.getComments(id)
            if(response.isSuccessful){
                response.body()?.let {
                    val body=it
                    Log.d("successCommentData",body.toString())
                    CommentsResult.Success(it)
                } ?:CommentsResult.Error(Exception("Body is null"))
            }
            else {
                Log.d("errorCommentData",response.errorBody().toString())
                 CommentsResult.Error(Exception("Error:  ${response.code()}"))

            }
        }catch (e:Exception){
            CommentsResult.Error(e)
        }
    }


}
interface NewsServie{
    @GET("api/4/news/latest")
    suspend fun getNews():Response<News>

    // 获取某个日期之前的新闻
    @GET("api/4/news/before/{date}")
    suspend fun getRecentNews(@Path("date") date: String): Response<News>

    @GET("api/4/news/before/{date}")
    suspend fun getRecentUrlNews(@Path("date") date: String): Response<News>

    @GET("api/4/story-extra/{id}")
    suspend fun getCommentnumber(@Path("id") id: Int): Response<Commentnumber>

    @GET("api/4/story/{id}/short-comments")
    suspend fun getComments(@Path("id") id: Int): Response<Comments>



}