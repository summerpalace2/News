package com.example.testwxy.viewmodel;

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/5/2 16:28
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(J\u0006\u0010)\u001a\u00020&J\u000e\u0010*\u001a\u00020&2\u0006\u0010+\u001a\u00020,J\u000e\u0010-\u001a\u00020&2\u0006\u0010+\u001a\u00020,J\u000e\u0010.\u001a\u00020&2\u0006\u0010\'\u001a\u00020(R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00100\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00120\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001d\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00100\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u001b\u0010\u001d\u001a\u00020\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b\u001f\u0010 R\u001d\u0010#\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00120\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0016\u00a8\u0006/"}, d2 = {"Lcom/example/testwxy/viewmodel/MainViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_commentResult", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/testwxy/repository/CommentsResult;", "Lcom/example/testwxy/repository/Comments;", "_commentnumberResult", "Lcom/example/testwxy/repository/CommentnumberResult;", "Lcom/example/testwxy/repository/Commentnumber;", "_newsResult", "Lcom/example/testwxy/repository/ResultData;", "Lcom/example/testwxy/repository/News;", "_recentNewsResult", "Lcom/example/testwxy/repository/ResultRecentData;", "_resulturlRecentNewsResult", "Lcom/example/testwxy/repository/ResultUrlRecentData;", "commentResult", "Landroidx/lifecycle/LiveData;", "getCommentResult", "()Landroidx/lifecycle/LiveData;", "commentnumberResult", "getCommentnumberResult", "newsResult", "getNewsResult", "recentNewsResult", "getRecentNewsResult", "repository", "Lcom/example/testwxy/repository/MainRepository;", "getRepository", "()Lcom/example/testwxy/repository/MainRepository;", "repository$delegate", "Lkotlin/Lazy;", "resulturlRecentNewsResult", "getResulturlRecentNewsResult", "getComment", "", "id", "", "getNews", "getRecentNews", "date", "", "getRecentNewsUrl", "getnumberComment", "app_debug"})
public final class MainViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy repository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.example.testwxy.repository.ResultData<com.example.testwxy.repository.News>> _newsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultData<com.example.testwxy.repository.News>> newsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.example.testwxy.repository.ResultRecentData<com.example.testwxy.repository.News>> _recentNewsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultRecentData<com.example.testwxy.repository.News>> recentNewsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.example.testwxy.repository.ResultUrlRecentData<com.example.testwxy.repository.News>> _resulturlRecentNewsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultUrlRecentData<com.example.testwxy.repository.News>> resulturlRecentNewsResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.example.testwxy.repository.CommentnumberResult<com.example.testwxy.repository.Commentnumber>> _commentnumberResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.example.testwxy.repository.CommentnumberResult<com.example.testwxy.repository.Commentnumber>> commentnumberResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.example.testwxy.repository.CommentsResult<com.example.testwxy.repository.Comments>> _commentResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.example.testwxy.repository.CommentsResult<com.example.testwxy.repository.Comments>> commentResult = null;
    
    public MainViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    private final com.example.testwxy.repository.MainRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultData<com.example.testwxy.repository.News>> getNewsResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultRecentData<com.example.testwxy.repository.News>> getRecentNewsResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.testwxy.repository.ResultUrlRecentData<com.example.testwxy.repository.News>> getResulturlRecentNewsResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.testwxy.repository.CommentnumberResult<com.example.testwxy.repository.Commentnumber>> getCommentnumberResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.example.testwxy.repository.CommentsResult<com.example.testwxy.repository.Comments>> getCommentResult() {
        return null;
    }
    
    public final void getNews() {
    }
    
    public final void getRecentNews(@org.jetbrains.annotations.NotNull
    java.lang.String date) {
    }
    
    public final void getRecentNewsUrl(@org.jetbrains.annotations.NotNull
    java.lang.String date) {
    }
    
    public final void getnumberComment(int id) {
    }
    
    public final void getComment(int id) {
    }
}