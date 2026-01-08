package com.zfx.wanandroidcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * 导航状态管理
 * 统一管理导航相关的状态和逻辑
 */
class NavState(
    val navController: NavController
) {
    /**
     * 当前路由
     */
    val currentRoute: String?
        get() = navController.currentBackStackEntry?.destination?.route

    /**
     * 是否显示底部导航栏
     */
    val showBottomBar: Boolean
        get() = currentRoute in Routes.routesWithBottomBar

    /**
     * 是否显示全局 TopBar
     */
    val showGlobalTopBar: Boolean
        get() = currentRoute in Routes.routesWithGlobalTopBar

    /**
     * 当前页面标题
     */
    val currentTitle: String
        get() = when (currentRoute) {
            Routes.HOME -> "玩Android"
            Routes.KNOWLEDGE -> "知识体系"
            Routes.WECHAT -> "公众号"
            else -> "玩Android"
        }

    /**
     * 是否可以返回
     */
    val canGoBack: Boolean
        get() = navController.previousBackStackEntry != null
}

/**
 * 记住导航状态
 */
@Composable
fun rememberNavState(navController: NavController): NavState {
    // 监听返回栈变化，触发重组
    val backStackEntry by navController.currentBackStackEntryAsState()
    
    return remember(navController, backStackEntry) {
        NavState(navController)
    }
}

