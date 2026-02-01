package com.example.testwxy.feature.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.AsyncImage
import com.example.testwxy.R
import com.example.testwxy.core.utils.Tool
import com.example.testwxy.core.utils.getWindowScreenSize
import com.example.testwxy.data.model.NewsItems
import com.example.testwxy.data.model.Story
import com.example.testwxy.data.model.TopStory
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider

/**
 * description ： TODO:知乎日报主页面
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2026/1/30 20:24
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(
    newsItems: List<NewsItems>?, // 混合列表数据
    onStoryClick: (Story) -> Unit,
    onBnnerClick: (TopStory) -> Unit,
    onAvatarClick: () -> Unit,
    onLoadMore: () -> Unit, // 触底回调
) {
    if (newsItems.isNullOrEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("加载中...")
        }
        return
    }
//主页rv的监听器逻辑
    //LazyColumn 的滚动状态
    val listState = rememberLazyListState()

    //当倒数第2个可见时触发加载
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    //触底加载
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    val windowSize = getWindowScreenSize()
//约束布局的设置
    ConstraintLayout(
        constraintSet = createConstraintSet(),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
//标题compose
        ToolbarCompose(
            modifier = Modifier.layoutId(MainElement.Toolbar), onAvatarClick
        )
//混合列表compose
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn(
                state = listState, // 绑定状态
                modifier = Modifier
                    .layoutId(MainElement.ContentList)
                    .fillMaxSize()
            ) {
                items(newsItems) { item ->
                    when (item) {
                        is NewsItems.BannerHeader -> {
                            BannerCompose(topStories = item.topStories, onClick = onBnnerClick)
                        }

                        is NewsItems.DateHeader -> {
                            DateHeaderCompose(item.date)
                        }

                        is NewsItems.StoryItem -> {
                            StoryItemCompose(story = item.story, onClick = onStoryClick)
                        }

                        is NewsItems.Footer -> {
                            LoadingFooter()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun LoadingFooter() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Text("正在加载更多内容...", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun createConstraintSet(): ConstraintSet {
    val windowSize = getWindowScreenSize()
    //只有当 windowSize 改变时才重新创建
    return remember(windowSize) {
        ConstraintSet {
            MainConstraintSet(scope = this, windowSize = windowSize).createConstrain()
        }
    }
}

@Composable
fun DateHeaderCompose(date: String) {
    Text(
        text = date,
        fontSize = 15.sp,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, top = 20.dp, bottom = 8.dp)
    )
}

@Composable
fun ToolbarCompose(modifier: Modifier = Modifier, onAvatarClick: () -> Unit) {
    val currentOnAvatarClick by rememberUpdatedState(onAvatarClick)
    val onClick = remember { { currentOnAvatarClick() } }
    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = Tool.getDate().toString(),
            color = Color.Black,
            fontSize = 16.sp
        )

        //分割线 (tab)
        Box(
            modifier = Modifier
                .padding(start = 15.dp)
                .width(1.dp)
                .height(25.dp)
                .background(Color(0xFFCCCCCC))
        )

        Text(
            text = Tool.getTime(),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 15.dp)
        )

        //这个 Spacer 会占据所有剩余空间，把后面的头像推向最右侧
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.tou),
            contentDescription = "头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BannerCompose(
    modifier: Modifier = Modifier,
    onClick: (TopStory) -> Unit,
    topStories: List<TopStory>?,

    autoTick: Long = 5000L
) {
    val stories = topStories ?: emptyList()
    val itemCount = stories.size
    if (itemCount == 0) return


    val virtualCount = Int.MAX_VALUE
    val initialPage = (virtualCount / 2) - (virtualCount / 2 % itemCount)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { virtualCount }
    )

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val currentOnClick by rememberUpdatedState(onClick)
    // 这里的 lambda 需要接收一个 TopStory 参数
    val onBannerClick = remember {
        { story: TopStory -> currentOnClick(story) }
    }

    // 自动轮播
    LaunchedEffect(isDragged) {
        if (!isDragged && itemCount > 1) {
            while (true) {
                delay(autoTick)
                runCatching {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)

    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val actualIndex = page % itemCount
            val currentStory = stories[actualIndex]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(currentStory) }
            ) {
                AsyncImage(
                    model = currentStory.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.tou)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 500f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, bottom = 30.dp)
                ) {
                    Text(
                        text = currentStory.title,
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(260.dp),
                        maxLines = 2,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = currentStory.hint,
                        color = Color(0xFFCCCCCC),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(itemCount) { index ->
                val isSelected = (pagerState.currentPage % itemCount) == index


                //宽高切换动画(8dp圆 <-> 16dp长条)
                val width by animateDpAsState(if (isSelected) 16.dp else 8.dp, tween(200))
                val height by animateDpAsState(if (isSelected) 6.dp else 8.dp, tween(200))

                //缩放动画(0.8f <-> 1.2f)
                val scale by animateFloatAsState(if (isSelected) 1.2f else 0.8f, tween(200))

                //透明度动画(0.6f <-> 1.0f)
                val alpha by animateFloatAsState(if (isSelected) 1.0f else 0.6f, tween(200))

                //颜色动画(#80FFFFFF <-> #FFFFFF)
                val bgColor by animateColorAsState(
                    if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                    tween(200)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                        .size(width = width, height = height)
                        .background(
                            color = bgColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun StoryItemCompose(story: Story, onClick: (Story) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick(story) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = story.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = story.hint,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        AsyncImage(
            model = story.images?.getOrNull(0),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}
