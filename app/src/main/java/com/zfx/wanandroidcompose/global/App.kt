package com.zfx.wanandroidcompose.global

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.zfx.commonlib.network.config.NetworkConfigBuilder
import com.zfx.commonlib.network.extension.initNetworkManager
import com.zfx.commonlib.network.interceptor.LoginInterceptor
import com.zfx.commonlib.network.repository.BaseRepository
import com.zfx.commonlib.util.StringResourceHelper
import com.zfx.wanandroidcompose.navigation.Routes
import com.zfx.wanandroidcompose.util.CookieInterceptor
import com.zfx.wanandroidcompose.util.NavControllerManager

class App : Application(), SingletonImageLoader.Factory {


    override fun onCreate() {
        super.onCreate()

        StringResourceHelper.init(this)
        initNetworkManager(
            NetworkConfigBuilder()
                .baseUrl("https://www.wanandroid.com")
                .addInterceptor(CookieInterceptor())
                .build()
        )

        BaseRepository.setLoginInterceptor(object : LoginInterceptor{
            override fun onUnauthorized(errorCode: Int, errorMessage: String) {
                // 未授权时，跳转到登录页面
                NavControllerManager.navigate(Routes.ACCOUNT)
            }
        }).unauthorizedCodes(setOf(1001))
            .interceptWindowMillis(3000)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}