package com.zfx.wanandroidcompose.feature.account

import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.zfx.commonlib.base.viewmodel.BaseViewModel
import com.zfx.commonlib.ext.collectResult
import com.zfx.wanandroidcompose.data.AccountBody
import com.zfx.wanandroidcompose.util.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountViewModel : BaseViewModel() {


    private val TAG = "AccountViewModel"
    private val repository = AccountRepository()


    //登录状态
    private val _signInState = MutableStateFlow(false)
    val signInState : StateFlow<Boolean> = _signInState.asStateFlow()

    //注册状态
    private val _registerState = MutableStateFlow(false)
    val registerState : StateFlow<Boolean> = _registerState.asStateFlow()

    //退出状态
    private val _signOutState = MutableStateFlow(false)
    val signOutState : StateFlow<Boolean> = _signOutState.asStateFlow()



    fun signIn(body : AccountBody){
        collectResult(
            flow = repository.signIn(body),
            onError = { error ->
                ToastUtils.showShort(error.message)
                Log.d(TAG,"登录错误返回的错误码 = ${error.code}，结果为${error.message}")
            },
            onSuccess = { user ->
                // 登录成功，保存用户信息
                // 用户名从响应体中获取（因为响应体中有 username）
                ToastUtils.showShort("登录成功")
                user.username?.let {
                    UserPreferences.saveUsername( it)
                    Log.d(TAG,"保存用户名: $it")
                }
                // token_pass 会通过 CookieInterceptor 自动从响应头中提取并保存
                _signInState.value = true
                Log.d(TAG,"登录成功，用户信息: $user")
            }
        )
    }


    fun register(body : AccountBody){
        collectResult(
            flow = repository.register(body),
            onError = { error ->
                ToastUtils.showShort(error.message)
                Log.d(TAG,"注册错误返回的错误码 = ${error.code}，结果为${error.message}")
            },
            onSuccess = { user ->
                ToastUtils.showShort("注册成功")
                _registerState.value = true
                Log.d(TAG,"注册成功，用户信息: $user")
            }
        )
    }

    fun signOut(){
        collectResult(
            flow = repository.signOut(),
            onError = { error ->
                ToastUtils.showShort(error.message)
                Log.d(TAG,"退出错误返回的错误码 = ${error.code}，结果为${error.message}")
            },
            onSuccess = { response ->
                // 退出成功，清除本地保存的用户信息
                UserPreferences.clear()
                _signOutState.value = true
                Log.d(TAG,"退出成功，已清除本地用户信息")
            }
        )
    }



}