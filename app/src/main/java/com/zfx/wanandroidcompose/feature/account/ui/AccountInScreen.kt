package com.zfx.wanandroidcompose.feature.account.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.data.AccountBody
import com.zfx.wanandroidcompose.feature.account.AccountViewModel


@Composable
fun AccountInScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: AccountViewModel = viewModel(),
    navController: NavController
){
    val signIn by viewModel.signInState.collectAsState()
    val register by viewModel.registerState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    LaunchedEffect(signIn,register) {
        if (signIn || register){
            navController.popBackStack()
        }
    }


    // 设置状态栏颜色为白色（onPrimary），退出时恢复为主题色
    val view = LocalView.current
    val statusBarColor = MaterialTheme.colorScheme.onPrimary  // 白色背景
    val primaryColor = MaterialTheme.colorScheme.primary     // 主题色（用于恢复）
    
    DisposableEffect(statusBarColor) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            // 设置状态栏背景色为白色
            // 使用 @Suppress("DEPRECATION") 抑制废弃警告（在 Android 14 及以下仍可用）
            @Suppress("DEPRECATION")
            it.statusBarColor = statusBarColor.toArgb()
            
            // 设置状态栏图标为深色（因为背景是白色）
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = true  // 深色图标（浅色背景）
            }
        }
        
        onDispose {
            // 恢复原来的状态栏设置
            val window = (view.context as? android.app.Activity)?.window
            window?.let {
                // 恢复为主题色
                @Suppress("DEPRECATION")
                it.statusBarColor = primaryColor.toArgb()
                
                // 恢复状态栏图标为浅色（主题色通常是深色）
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = false  // 浅色图标（深色背景）
                }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 32.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable{
                        navController.popBackStack()
                    },
                painter = painterResource(R.drawable.icon_close_grey),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                contentDescription = stringResource(R.string.account_close_button)
            )
        }

        Spacer(Modifier.size(20.dp))

        Image(
            modifier = Modifier
                .size(64.dp),
            painter = painterResource(R.drawable.icon_avatar_default),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            contentDescription = stringResource(R.string.account_default_avatar)
        )

        Spacer(Modifier.size(20.dp))

        // 用户名输入框
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.account_username)) },
            placeholder = { Text(stringResource(R.string.account_username_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.size(10.dp))

        // 密码输入框
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.account_password)) },
            placeholder = { Text(stringResource(R.string.account_password_hint)) },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) stringResource(R.string.account_hide_password) else stringResource(R.string.account_show_password)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.size(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val userToast = stringResource(R.string.account_username_empty)
            val pwdToast = stringResource(R.string.account_password_empty)
            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (username.isEmpty()){
                        ToastUtils.showShort(userToast)
                        return@Button
                    }
                    if (password.isEmpty()){
                        ToastUtils.showShort(pwdToast)
                        return@Button
                    }
                    viewModel.register(AccountBody(username = username, password = password, repassword = password))
                }
            ) {
                Text(stringResource(R.string.account_register))
            }

            Spacer(Modifier.size(20.dp))

            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (username.isEmpty()){
                        ToastUtils.showShort(userToast)
                        return@Button
                    }
                    if (password.isEmpty()){
                        ToastUtils.showShort(pwdToast)
                        return@Button
                    }
                    viewModel.signIn(AccountBody(username = username, password = password))
                }
            ) {
                Text(stringResource(R.string.account_login))
            }
        }


    }



}