package com.example.testwxy.personal;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u001eH\u0002J\u001c\u0010 \u001a\u00020\u001e2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00042\u0006\u0010#\u001a\u00020$J\b\u0010%\u001a\u00020\u001eH\u0002J\u0012\u0010&\u001a\u00020\u001e2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0014J\b\u0010)\u001a\u00020\u001eH\u0002J\b\u0010*\u001a\u00020\u001eH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u000b\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/example/testwxy/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "bannerList", "", "Lcom/example/testwxy/repository/TopStory;", "binding", "Lcom/example/testwxy/databinding/ActivityMainBinding;", "getBinding", "()Lcom/example/testwxy/databinding/ActivityMainBinding;", "binding$delegate", "Lkotlin/Lazy;", "indicatorLayout", "Landroid/widget/LinearLayout;", "isLoading", "", "newsAdapter", "Lcom/example/testwxy/adapter/NewsAdapter;", "number", "", "sharedPreferences", "Landroid/content/SharedPreferences;", "getSharedPreferences", "()Landroid/content/SharedPreferences;", "sharedPreferences$delegate", "viewModel", "Lcom/example/testwxy/viewmodel/MainViewModel;", "viewPager", "Landroidx/viewpager2/widget/ViewPager2;", "initEvent", "", "initView", "loadMoreItems", "newItems", "Lcom/example/testwxy/repository/Story;", "data", "", "observeRecycleview", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onReachBottom", "setupObservers", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.testwxy.adapter.NewsAdapter newsAdapter;
    private androidx.viewpager2.widget.ViewPager2 viewPager;
    private android.widget.LinearLayout indicatorLayout;
    private java.util.List<com.example.testwxy.repository.TopStory> bannerList;
    private com.example.testwxy.viewmodel.MainViewModel viewModel;
    private int number = -1;
    private boolean isLoading = false;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy binding$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy sharedPreferences$delegate = null;
    
    public MainActivity() {
        super();
    }
    
    private final com.example.testwxy.databinding.ActivityMainBinding getBinding() {
        return null;
    }
    
    private final android.content.SharedPreferences getSharedPreferences() {
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
    
    private final void setupObservers() {
    }
    
    private final void observeRecycleview() {
    }
    
    private final void onReachBottom() {
    }
    
    public final void loadMoreItems(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.testwxy.repository.Story> newItems, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
}