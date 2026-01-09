package com.zfx.wanandroidcompose.feature.setting.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.feature.setting.SettingViewModel
import com.zfx.wanandroidcompose.feature.setting.settingViewModel


@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel? = null
){
    // 如果传入了 viewModel，使用传入的；否则创建新的
    val settingViewModel = viewModel ?: settingViewModel()

    val theme by settingViewModel.curTheme.collectAsState()
    
    // 控制主题选择对话框的显示
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    ),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(
                    Modifier.size(24.dp)
                )

                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                                   },
                    painter = painterResource(R.drawable.icon_back_white),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    contentDescription = "返回按钮"
                )

                Spacer(
                    Modifier.size(48.dp)
                )

                Text(
                    text = "设置",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.onPrimary
                )
        ) {

            SettingItem(
                name = "主题设置",
                setName = theme.themeName,
                onItemClick = {
                    showThemeDialog = true  // 显示对话框
                }
            )
            
            // 主题选择对话框
            if (showThemeDialog) {
                ThemeSelectDialog(
                    onSelect = { selectedTheme ->
                        settingViewModel.setTheme(selectedTheme)  // 保存选中的主题
                        showThemeDialog = false  // 关闭对话框
                    },
                    onDismiss = {
                        showThemeDialog = false  // 关闭对话框
                    }
                )
            }


            SettingItem(
                name = "语言设置",
                setName = "跟随系统",
                onItemClick = {

                }
            )

        }

    }


}


@Composable
fun SettingItem(
    name : String,
    setName : String,
    onItemClick : () -> Unit
){
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clickable{
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
            fontSize = 16.sp
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = setName,
            color = if (name == "主题设置") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onTertiary,
            fontSize = 16.sp
        )

        Spacer(Modifier.size(12.dp))

        Image(
            painter = painterResource(R.drawable.icon_arrow_right_gray),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentDescription = "向右箭头"
        )

    }
}