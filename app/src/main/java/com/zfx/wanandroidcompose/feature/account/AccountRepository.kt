package com.zfx.wanandroidcompose.feature.account

import com.zfx.commonlib.network.extension.getApiService
import com.zfx.commonlib.network.repository.BaseRepository
import com.zfx.commonlib.network.result.NetworkResult
import com.zfx.wanandroidcompose.data.AccountBody
import com.zfx.wanandroidcompose.data.User
import com.zfx.wanandroidcompose.feature.coin.data.CoinData
import com.zfx.wanandroidcompose.feature.coin.data.PersonalCoinData
import com.zfx.wanandroidcompose.service.AccountService
import kotlinx.coroutines.flow.Flow

class AccountRepository  : BaseRepository() {


    private val apiService by lazy {
        getApiService<AccountService>()
    }


    /**
     * 登录
     * 成功时返回用户信息，失败时返回错误信息（无data）
     * */
    fun signIn(body : AccountBody) : Flow<NetworkResult<User>>{
        return requestFlow(
            apiCall = {
                apiService.signIn(
                    username = body.username ?: "",
                    password = body.password ?: ""
                )
            }
        )
    }


    /**
     * 注册
     * */
    fun register(body : AccountBody) : Flow<NetworkResult<User>>{
        return requestFlow(
            apiCall = {
                apiService.register(
                    username = body.username ?: "",
                    password = body.password ?: "",
                    repassword = body.repassword ?: ""
                )
            }
        )
    }

    /**
     * 退出
     * */
    fun signOut() : Flow<NetworkResult<String>>{
        return requestFlow(
            apiCall = {
                apiService.signOut()
            }
        )
    }


    fun getPersonalCoin() : Flow<NetworkResult<CoinData>>{
        return requestFlow(
            apiCall = {
                apiService.getPersonalCoin()
            }
        )
    }

}