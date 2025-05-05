package com.example.testwxy.repository;

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 15:50
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u001f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u001f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00150\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u001f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0019\u001a\u00020\u001aH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bR#\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/example/testwxy/repository/MainRepository;", "", "()V", "retrofit", "Lcom/example/testwxy/repository/NewsServie;", "kotlin.jvm.PlatformType", "getRetrofit", "()Lcom/example/testwxy/repository/NewsServie;", "retrofit$delegate", "Lkotlin/Lazy;", "getComment", "Lcom/example/testwxy/repository/CommentsResult;", "Lcom/example/testwxy/repository/Comments;", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommentnumber", "Lcom/example/testwxy/repository/CommentnumberResult;", "Lcom/example/testwxy/repository/Commentnumber;", "getNews", "Lcom/example/testwxy/repository/ResultData;", "Lcom/example/testwxy/repository/News;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentNews", "Lcom/example/testwxy/repository/ResultRecentData;", "date", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentNewsUrl", "Lcom/example/testwxy/repository/ResultUrlRecentData;", "app_debug"})
public final class MainRepository {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy retrofit$delegate = null;
    
    public MainRepository() {
        super();
    }
    
    private final com.example.testwxy.repository.NewsServie getRetrofit() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getNews(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.testwxy.repository.ResultData<com.example.testwxy.repository.News>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getRecentNews(@org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.testwxy.repository.ResultRecentData<com.example.testwxy.repository.News>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getRecentNewsUrl(@org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.testwxy.repository.ResultUrlRecentData<com.example.testwxy.repository.News>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getCommentnumber(int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.testwxy.repository.CommentnumberResult<com.example.testwxy.repository.Commentnumber>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getComment(int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.testwxy.repository.CommentsResult<com.example.testwxy.repository.Comments>> $completion) {
        return null;
    }
}