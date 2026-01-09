package com.zfx.wanandroidcompose.feature.us

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R


/**
 * 关于我们
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(
    modifier: Modifier = Modifier.fillMaxHeight().fillMaxWidth(),
    navController: NavController
){

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("关于我们", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "返回"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarColors(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)

        ) {

            item {
                Spacer(Modifier.size(20.dp))

                Text(
                    text = "Compose版玩Android",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
            }

            item {
                Spacer(Modifier.size(20.dp))

                Text(
                    text = "# Compose 可折叠 Toolbar 使用指南\n" +
                            "\n" +
                            "## 概述\n" +
                            "\n" +
                            "本指南对比 View 系统中的 `CoordinatorLayout` + `AppBarLayout` + `CollapsingToolbarLayout` 组合与 Compose 中的对应实现方式。\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "## View 系统 vs Compose 组件对比\n" +
                            "\n" +
                            "| View 系统 | Compose | 功能说明 |\n" +
                            "|-----------|---------|---------|\n" +
                            "| `CoordinatorLayout` | `Modifier.nestedScroll()` | 嵌套滚动行为协调器 |\n" +
                            "| `AppBarLayout` | `TopAppBar` | 标准 Toolbar |\n" +
                            "| `CollapsingToolbarLayout` | `LargeTopAppBar` / `MediumTopAppBar` | 可折叠 Toolbar |\n" +
                            "\n" +
                            "---\n" +
                            "\n" +
                            "## View 系统实现方式\n" +
                            "\n" +
                            "### 1. CoordinatorLayout + AppBarLayout + CollapsingToolbarLayout",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }

            item {
                Spacer(Modifier.size(20.dp))

                Text(
                    text = "开源代码地址https://github.com/zhufeixiang/WanAndroidCompose",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }

        }
    }


}