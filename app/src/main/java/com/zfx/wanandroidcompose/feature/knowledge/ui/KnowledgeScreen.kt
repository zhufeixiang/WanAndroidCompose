package com.zfx.wanandroidcompose.feature.knowledge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.feature.navigation.ui.NavigationScreen
import kotlinx.coroutines.launch


@Composable
fun KnowledgeScreen(
    navController: NavController? = null,
    onToggleBars: (Boolean) -> Unit = {},
    nestedScrollConnection: NestedScrollConnection? = null
){



    val tabData = remember {
        listOf("体系","导航")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { tabData.size }
        )
        val scope = rememberCoroutineScope()

        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            edgePadding = 0.dp, // 移除左右边距
            // 不设置 minTabWidth，让 Tab 根据内容自适应宽度
            indicator = {
                // PrimaryScrollableTabRow 使用 PrimaryIndicator
                // indicator 是 TabIndicatorScope 的扩展函数，不需要显式参数
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        selectedTabIndex = pagerState.currentPage,
                        matchContentSize = true
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,   // 指示器颜色
                    height = 2.dp                // 指示器高度
                )
            }
        ) {
            tabData.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            key = { page -> page }
        ) { page ->
            if (page == 0){
                KnowledgePage(navController = navController, onToggleBars = onToggleBars, nestedScrollConnection = nestedScrollConnection)
            }else{
                NavigationScreen(navController = navController, onToggleBars = onToggleBars, nestedScrollConnection = nestedScrollConnection)
            }
        }
    }


}