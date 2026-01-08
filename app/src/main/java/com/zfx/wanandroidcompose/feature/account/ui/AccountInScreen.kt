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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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


    // 设置状态栏为白色
    val view = LocalView.current
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            // 设置状态栏颜色为白色（使用颜色值，不是资源 ID）
            it.statusBarColor = ContextCompat.getColor(context, R.color.white)

            // 设置状态栏图标为深色（因为背景是白色）
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = true
            }
        }

        onDispose {
            // 恢复原来的状态栏设置
            val window = (view.context as? android.app.Activity)?.window
            window?.let {
                // 恢复为主题色
                it.statusBarColor = ContextCompat.getColor(context, R.color.theme)

                // 恢复状态栏图标为浅色
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = false
                }
            }
        }
    }


    Column(
        modifier = modifier
            .background(color = colorResource(R.color.white))
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
                contentDescription = "关闭按钮"
            )
        }

        Spacer(Modifier.size(20.dp))

        Image(
            modifier = Modifier
                .size(64.dp),
            painter = painterResource(R.drawable.icon_avatar_default),
            contentDescription = "默认头像"
        )

        Spacer(Modifier.size(20.dp))

        // 用户名输入框
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            placeholder = { Text("请输入用户名") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.size(10.dp))

        // 密码输入框
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            placeholder = { Text("请输入密码") },
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
                        contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
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
            Button(
                colors = ButtonColors(
                    containerColor = colorResource(R.color.theme),
                    contentColor = colorResource(R.color.white),
                    disabledContentColor = colorResource(R.color.color_808A8A8A),
                    disabledContainerColor = colorResource(R.color.color_808A8A8A)
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (username.isEmpty()){
                        ToastUtils.showShort("请输入用户名！")
                        return@Button
                    }
                    if (password.isEmpty()){
                        ToastUtils.showShort("请输入密码！")
                        return@Button
                    }
                    viewModel.register(AccountBody(username = username, password = password, repassword = password))
                }
            ) {
                Text("注册")
            }

            Spacer(Modifier.size(20.dp))

            Button(
                colors = ButtonColors(
                    containerColor = colorResource(R.color.theme),
                    contentColor = colorResource(R.color.white),
                    disabledContentColor = colorResource(R.color.color_808A8A8A),
                    disabledContainerColor = colorResource(R.color.color_808A8A8A)
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (username.isEmpty()){
                        ToastUtils.showShort("请输入用户名！")
                        return@Button
                    }
                    if (password.isEmpty()){
                        ToastUtils.showShort("请输入密码！")
                        return@Button
                    }
                    viewModel.signIn(AccountBody(username = username, password = password))
                }
            ) {
                Text("登录")
            }
        }


    }



}