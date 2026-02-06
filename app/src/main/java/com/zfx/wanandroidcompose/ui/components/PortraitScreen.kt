package com.zfx.wanandroidcompose.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zfx.wanandroidcompose.feature.setting.SettingViewModel
import com.zfx.wanandroidcompose.navigation.NavState
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.navigation.rememberNavState
import com.zfx.wanandroidcompose.navigation.setupNavigation
import com.zfx.wanandroidcompose.util.NavControllerManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitScreen(
    settingViewModel : SettingViewModel
){
    val navController = rememberNavController()

    // 注册 NavController 到全局管理器
    DisposableEffect(navController) {
        NavControllerManager.registerNavController(navController)
        onDispose {
            NavControllerManager.unregisterNavController()
        }
    }
    // 使用导航状态管理，统一管理导航相关状态
    val navState = rememberNavState(navController)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // BottomBar 显隐状态
    val barsVisible = remember { mutableStateOf(true) }

    // BottomBar 的透明度与位移动画（覆盖在内容上方）
    val bottomBarAlpha by animateFloatAsState(
        targetValue = if (barsVisible.value && navState.showBottomBar) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 400f
        ),
        label = "bottomBarAlpha"
    )
    val bottomBarTranslationY by animateFloatAsState(
        targetValue = if (barsVisible.value && navState.showBottomBar) 0f else 56f,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 400f
        ),
        label = "bottomBarTranslationY"
    )

    // 全局 TopBar 的滚动行为
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                modifier = Modifier.width(200.dp),
                drawerState = drawerState
            ) {
                DrawerContent(
                    navController = navController,
                    drawerState = drawerState
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                // 只在需要显示全局 TopBar 的路由中显示
                if (navState.showGlobalTopBar) {
                    ScrollableTopBar(
                        title = stringResource(navState.currentTitleResId),
                        drawerState = drawerState,
                        scrollBehavior = scrollBehavior,
                        onSearchClick = {
                            navController.navigate(Routes.SEARCH)
                        }
                    )
                }
            }
        ) { paddingValues ->
            // 使用 Box 包裹，让 BottomBar 可以覆盖在内容上方
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 使用统一的导航配置，传递 drawerState 和 nestedScrollConnection
                    setupNavigation(
                        navController = navController,
                        onToggleBars = { show -> barsVisible.value = show },
                        drawerState = drawerState,
                        nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                        settingViewModel = settingViewModel
                    )
                }

                // BottomBar - 覆盖在内容上方（不占用布局空间）
                if (navState.showBottomBar) {
                    Box(
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.BottomCenter)
                            .graphicsLayer {
                                alpha = bottomBarAlpha
                                translationY = bottomBarTranslationY
                            }
                    ) {
                        BottomNavigationBar(
                            navController = navController,
                            currentRoute = navState.currentRoute
                        )

//                            FloatingActionButton(
//                                modifier = Modifier
//                                    .align(Alignment.BottomEnd)
//                                    .offset(x = (-24).dp, y = (-68).dp)
//                                    .size(56.dp),  // 确保宽高相等，标准 FAB 尺寸
//                                onClick = {
//                                    ToastUtils.showShort("列表滑到顶部")
//                                },
//                                containerColor = colorResource(R.color.theme),
//                                contentColor = colorResource(R.color.white),
//                                shape = CircleShape  // 显式设置为圆形
//                            ) {
//                                Image(
//                                    painter = painterResource(R.drawable.icon_arrow_up),
//                                    modifier = Modifier.size(24.dp),
//                                    contentDescription = "向上箭头",
//                                    colorFilter = ColorFilter.tint(colorResource(R.color.white))  // 使用 colorFilter 改变颜色
//                                )
//                            }
                    }


                }


            }
        }
    }

}