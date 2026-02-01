package com.example.testwxy.feature.home

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSetScope
import androidx.constraintlayout.compose.Dimension

/**
 * description ： TODO:知乎日报主界面的Constraint约束
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2026/1/30 20:56
 */
enum class MainElement {
    Toolbar,
    ContentList
}

class MainConstraintSet(
    val scope: ConstraintSetScope,
    val windowSize: DpSize
) {
    val toolbar = scope.createRefFor(MainElement.Toolbar)
    val contentList = scope.createRefFor(MainElement.ContentList) // 统一 ID

    fun createConstrain() {
        scope.constrain(toolbar) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.value(50.dp)
        }

        scope.constrain(contentList) {
            top.linkTo(toolbar.bottom) // 紧贴 Toolbar 底部
            bottom.linkTo(parent.bottom) // 底部对齐父容器
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints // 填充剩余所有空间
        }
    }
}
