package com.zfx.wanandroidcompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.ToastUtils
import com.zfx.wanandroidcompose.R
import kotlinx.coroutines.launch

/**
 * 顶部导航栏内容（可复用组件）
 * 用于在 TopAppBar 中显示
 */
@Composable
fun TopBarContent(
    title: String,
    drawerState: androidx.compose.material3.DrawerState,
    onSearchClick: () -> Unit = { ToastUtils.showShort("点击了搜索") }
) {
    val scope = rememberCoroutineScope()
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    drawerState.open()
                }
            }
        ) {
            Image(
                painter = painterResource(R.drawable.icon_menu_white),
                contentDescription = "目录",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            color = colorResource(R.color.white),
            fontSize = 18.sp
        )

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onSearchClick) {
            Image(
                painter = painterResource(R.drawable.icon_search_white),
                contentDescription = "搜索",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * 顶部导航栏（使用 TopAppBar + nestedScroll，支持整体滚动）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTopBar(
    title: String,
    drawerState: androidx.compose.material3.DrawerState,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit = { ToastUtils.showShort("点击了搜索") }
) {
    val scope = rememberCoroutineScope()
    
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_menu_white),
                    contentDescription = "目录",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Image(
                    painter = painterResource(R.drawable.icon_search_white),
                    contentDescription = "搜索",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        scrollBehavior = scrollBehavior
    )
}

/**
 * 顶部导航栏（旧版本，保持兼容性）
 */
@Composable
fun TopBar(
    currentRoute: String?,
    drawerState: androidx.compose.material3.DrawerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    
    val title = when (currentRoute) {
        com.zfx.wanandroidcompose.navigation.Routes.HOME -> "玩Android"
        com.zfx.wanandroidcompose.navigation.Routes.KNOWLEDGE -> "知识体系"
        com.zfx.wanandroidcompose.navigation.Routes.WECHAT -> "公众号"
        else -> "玩Android"
    }
    
    TopBarContent(
        title = title,
        drawerState = drawerState
    )
}



