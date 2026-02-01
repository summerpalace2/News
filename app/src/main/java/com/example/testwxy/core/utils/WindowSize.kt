package com.example.testwxy.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun getWindowScreenSize(): DpSize {
    // 获取屏幕配置
    val configuration = LocalConfiguration.current
    // 返回具体的 DpSize（包含具体的宽和高）
    return DpSize(
        width = configuration.screenWidthDp.dp,
        height = configuration.screenHeightDp.dp
    )
}