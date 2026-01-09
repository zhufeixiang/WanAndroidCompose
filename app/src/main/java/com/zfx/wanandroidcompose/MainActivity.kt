package com.zfx.wanandroidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.ToastUtils
import com.zfx.wanandroidcompose.feature.setting.SettingViewModel
import com.zfx.wanandroidcompose.feature.setting.SettingViewModelFactory
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.navigation.setupNavigation
import com.zfx.wanandroidcompose.navigation.rememberNavState
import com.zfx.wanandroidcompose.ui.components.BottomNavigationBar
import com.zfx.wanandroidcompose.ui.components.DrawerContent
import com.zfx.wanandroidcompose.ui.components.ScrollableTopBar
import com.zfx.wanandroidcompose.ui.theme.AppTheme
import com.zfx.wanandroidcompose.ui.theme.WanAndroidComposeTheme
import com.zfx.wanandroidcompose.util.NavControllerManager

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }

    @Composable
    private fun MainScreen() {
        val context = LocalContext.current
        val settingViewModel: SettingViewModel = viewModel(
            factory = SettingViewModelFactory(context)
        )
        val theme by settingViewModel.curTheme.collectAsState()
        WanAndroidComposeTheme(
            theme = theme
        ){
            AppPortrait(settingViewModel = settingViewModel)
        }
    }


    /**
     * 竖屏布局
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AppPortrait(settingViewModel: SettingViewModel) {
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
                            title = navState.currentTitle,
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





}