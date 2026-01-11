package com.zfx.wanandroidcompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.navigation.navigateToAccount
import com.zfx.wanandroidcompose.navigation.navigateToAboutUs
import com.zfx.wanandroidcompose.navigation.navigateToSetting
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
            .background(MaterialTheme.colorScheme.background),
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
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(R.string.drawer_user_avatar)
        )

        Spacer(Modifier.size(12.dp))

        Text(
            text = userName?.takeIf { it.isNotEmpty() } ?: stringResource(R.string.drawer_please_login),
            color = MaterialTheme.colorScheme.surface,
            fontSize = 16.sp
        )

        Spacer(Modifier.size(28.dp))

        DrawerItem(
            onItemClick = {

            },
            imageRes = R.drawable.icon_favorite,
            name = stringResource(R.string.drawer_my_favorites)
        )

        Spacer(Modifier.size(2.dp))

        DrawerItem(
            onItemClick = {
                scope.launch {
                    drawerState.close()
                }
                navController.navigateToSetting()
            },
            imageRes = R.drawable.icon_setting,
            name = stringResource(R.string.drawer_system_settings)
        )

        Spacer(Modifier.size(2.dp))

        DrawerItem(
            onItemClick = {
                scope.launch {
                    drawerState.close()
                }
                navController.navigateToAboutUs()
            },
            imageRes = R.drawable.icon_about_us,
            name = stringResource(R.string.drawer_about_us)
        )



    }
}

@Composable
fun DrawerItem(
    onItemClick : () -> Unit,
    imageRes : Int,
    name : String
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .clickable{
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(imageRes),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
            contentDescription = name
        )

        Spacer(Modifier.size(24.dp))

        Text(
            text = name,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 14.sp
        )

    }

}


