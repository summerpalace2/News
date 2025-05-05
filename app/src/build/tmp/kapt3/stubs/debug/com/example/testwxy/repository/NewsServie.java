package com.example.testwxy.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J!\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ!\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J!\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2 = {"Lcom/example/testwxy/repository/NewsServie;", "", "getCommentnumber", "Lretrofit2/Response;", "Lcom/example/testwxy/repository/Commentnumber;", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getComments", "Lcom/example/testwxy/repository/Comments;", "getNews", "Lcom/example/testwxy/repository/News;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentNews", "date", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentUrlNews", "app_debug"})
public abstract interface NewsServie {
    
    @retrofit2.http.GET(value = "api/4/news/latest")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getNews(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.testwxy.repository.News>> $completion);
    
    @retrofit2.http.GET(value = "api/4/news/before/{date}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getRecentNews(@retrofit2.http.Path(value = "date")
    @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.testwxy.repository.News>> $completion);
    
    @retrofit2.http.GET(value = "api/4/news/before/{date}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getRecentUrlNews(@retrofit2.http.Path(value = "date")
    @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.testwxy.repository.News>> $completion);
    
    @retrofit2.http.GET(value = "api/4/story-extra/{id}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getCommentnumber(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.testwxy.repository.Commentnumber>> $completion);
    
    @retrofit2.http.GET(value = "api/4/story/{id}/short-comments")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getComments(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.testwxy.repository.Comments>> $completion);
}