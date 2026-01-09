package com.zfx.wanandroidcompose.feature.wechat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.feature.wechat.WechatViewModel
import kotlinx.coroutines.launch


@Composable
fun WechatScreen(
    viewModel: WechatViewModel = viewModel(),
    navController: NavController,
    onToggleBars: (Boolean) -> Unit = {},
    nestedScrollConnection: androidx.compose.ui.input.nestedscroll.NestedScrollConnection? = null
){
    val accountList by viewModel.wechatAccounts.collectAsState()


    val tabData = remember(accountList) {
        accountList.map { it.name }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 确保有子节点才显示内容
        if (accountList.isNotEmpty()) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { accountList.size }
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
                key = { page -> accountList[page].id }
            ) { page ->
                WechatPage(
                    accountId =  accountList[page].id,
                    isActive = pagerState.currentPage == page,
                    viewModel = viewModel,
                    navController = navController,
                    onToggleBars = onToggleBars,
                    nestedScrollConnection = nestedScrollConnection
                )
            }
        }
    }


}