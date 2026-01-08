package com.zfx.wanandroidcompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.navigation.navigateToAccount
import com.zfx.wanandroidcompose.navigation.navigateToAboutUs
import com.zfx.wanandroidcompose.util.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 抽屉内容
 */
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: androidx.compose.material3.DrawerState,
    modifier: Modifier = Modifier
) {
    // 使用 remember + mutableStateOf 来保存用户名状态
    var userName by remember { mutableStateOf(UserPreferences.getUsername()) }

    // 当 Drawer 打开时，重新读取用户名（这样可以响应登录后的变化）
    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen) {
            userName = UserPreferences.getUsername()
        }
    }

    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(28.dp))

        Icon(
            modifier = Modifier
                .size(64.dp)
                .then(
                    // 只有在未登录时才可点击
                    if (userName.isNullOrEmpty()) {
                        Modifier.clickable {
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigateToAccount()
                        }
                    } else {
                        Modifier
                    }
                ),
            painter = painterResource(R.mipmap.icon_app_logo),
            tint = colorResource(R.color.theme),
            contentDescription = "用户头像"
        )

        Spacer(Modifier.size(12.dp))

        Text(
            text = userName?.takeIf { it.isNotEmpty() } ?: "请先登录",
            color = colorResource(R.color.black),
            fontSize = 16.sp
        )

        Spacer(Modifier.size(28.dp))
        
        NavigationDrawerItem(
            label = {
                Text(
                    "我的收藏",
                    color = colorResource(R.color.black),
                    fontSize = 18.sp
                )
            },
            icon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.icon_favorite),
                    tint = colorResource(R.color.theme),
                    contentDescription = "我的收藏"
                )
            },
            selected = false,
            onClick = {
                // TODO: 实现收藏页面导航
            },
        )

        Spacer(Modifier.size(2.dp))

        NavigationDrawerItem(
            label = {
                Text(
                    "关于我们",
                    color = colorResource(R.color.black),
                    fontSize = 18.sp
                )
            },
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.icon_about_us),
                    tint = colorResource(R.color.theme),
                    contentDescription = "关于我们"
                )
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                navController.navigateToAboutUs()
            },
        )
    }
}


