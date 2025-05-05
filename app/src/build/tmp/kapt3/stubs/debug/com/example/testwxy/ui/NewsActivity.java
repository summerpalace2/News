package com.example.testwxy.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0002J\u0010\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0002J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\u001e\u001a\u00020\u001aH\u0002J\b\u0010\u001f\u001a\u00020\u001aH\u0002J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0002J\b\u0010!\u001a\u00020\u001aH\u0002J\u0012\u0010\"\u001a\u00020\u001a2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\b\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/example/testwxy/ui/NewsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/example/testwxy/databinding/ActivityNewsBinding;", "getBinding", "()Lcom/example/testwxy/databinding/ActivityNewsBinding;", "binding$delegate", "Lkotlin/Lazy;", "commentAdapter", "Lcom/example/testwxy/adapter/NewsUrladapter;", "id", "", "Ljava/lang/Integer;", "number", "previousPosition", "url", "", "viewModel", "Lcom/example/testwxy/viewmodel/MainViewModel;", "getViewModel", "()Lcom/example/testwxy/viewmodel/MainViewModel;", "viewModel$delegate", "viewpager", "Landroidx/viewpager2/widget/ViewPager2;", "getOnceListUrl", "", "date", "getTwiceListUrl", "getcomment", "initEvent", "initView", "listener", "observe", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class NewsActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.Nullable
    private java.lang.Integer id;
    @org.jetbrains.annotations.Nullable
    private java.lang.String url;
    private int number = 0;
    private com.example.testwxy.adapter.NewsUrladapter commentAdapter;
    private androidx.viewpager2.widget.ViewPager2 viewpager;
    private int previousPosition = 0;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy binding$delegate = null;
    
    public NewsActivity() {
        super();
    }
    
    private final com.example.testwxy.viewmodel.MainViewModel getViewModel() {
        return null;
    }
    
    private final com.example.testwxy.databinding.ActivityNewsBinding getBinding() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initEvent() {
    }
    
    private final void initView() {
    }
    
    private final void listener(java.lang.String date) {
    }
    
    private final void getcomment(int id) {
    }
    
    private final void getOnceListUrl(java.lang.String date) {
    }
    
    private final void getTwiceListUrl(java.lang.String date) {
    }
    
    private final void observe() {
    }
}