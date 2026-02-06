package com.zfx.wanandroidcompose.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.blankj.utilcode.util.ToastUtils
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.feature.home.HomeViewModel
import com.zfx.wanandroidcompose.data.Article
import com.zfx.commonlib.ext.compose.Banner
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.ui.components.RefreshableLazyList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onArticleClick: ((Article) -> Unit)? = null,
    navController: NavController? = null,
    onToggleBars: (Boolean) -> Unit = {},
    drawerState: androidx.compose.material3.DrawerState? = null,
    nestedScrollConnection: NestedScrollConnection? = null
){
    val bannerList by viewModel.bannerList.collectAsState()
    val topArticleList by viewModel.topArticle.collectAsState()
    val articleList by viewModel.articleList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMore by viewModel.hasMore.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()

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
        items = topArticleList + articleList,
        modifier = modifier.fillMaxWidth(),
        isLoading = isLoading,
        hasMore = hasMore,
        isRefreshing = isRefreshing || isInitialLoading,  // 初始加载时也显示刷新指示器
        listState = listState,
        nestedScrollConnection = nestedScrollConnection,
        header = if (bannerList.isNotEmpty()) {
            {
                Banner(
                    modifier = Modifier.height(160.dp),
                    items = bannerList,
                    showIndicator = false,
                    indicator = { curPage, pageCount ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(pageCount) { index ->
                                Spacer(
                                    modifier = Modifier.size(1.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index == curPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                        )
                                )
                            }
                        }
                    },
                    pageContent = { pageItem ->
                        BannerContent(pageData = pageItem)
                    }
                )
            }
        } else null,
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
                onArticleClick?.invoke(article)
                navController?.let { nav ->
                    nav.navigate(Routes.buildLinkRoute(article.title, article.link))
                }
            }
        )
    }
}