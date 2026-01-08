package com.zfx.wanandroidcompose.feature.square.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.blankj.utilcode.util.ToastUtils
import com.zfx.wanandroidcompose.feature.home.ui.ArticleItem
import com.zfx.wanandroidcompose.feature.square.SquareViewModel
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.ui.components.RefreshableLazyList


@Composable
fun SquareScreen(
    viewModel: SquareViewModel = viewModel(),
    navController: NavController,
    onToggleBars: (Boolean) -> Unit = {},
    nestedScrollConnection: NestedScrollConnection? = null
){
    val articleList by viewModel.articles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val listState = rememberLazyListState()

    // 滚动时控制 BottomBar 显隐
    LaunchedEffect(listState) {
        var last = 0
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val current = index * 100000 + offset
                val delta = current - last
                if (kotlin.math.abs(delta) > 6) {
                    onToggleBars(delta < 0)
                    last = current
                }
                if (index == 0 && offset == 0) {
                    onToggleBars(true)
                }
            }
    }

    RefreshableLazyList(
        items = articleList,
        modifier = Modifier.fillMaxWidth(),
        isLoading = isLoading,
        hasMore = hasMore,
        isRefreshing = isRefreshing,
        listState = listState,
        nestedScrollConnection = nestedScrollConnection,
        itemKey = { article -> "${article.id}_${article.publishTime}" },
        onLoadMore = { viewModel.loadMore() },
        onRefresh = { viewModel.refresh() }
    ) { article ->
        ArticleItem(
            data = article,
            favoriteClick = {
                ToastUtils.showShort("收藏文章：${article.title}")
            },
            cardClick = {
                navController.navigate(Routes.buildLinkRoute(article.title, article.link))
            }
        )
    }
}