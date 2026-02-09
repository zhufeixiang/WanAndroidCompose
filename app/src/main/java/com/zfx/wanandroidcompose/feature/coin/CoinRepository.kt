package com.zfx.wanandroidcompose.feature.coin

import com.zfx.commonlib.network.extension.getApiService
import com.zfx.commonlib.network.repository.BaseRepository
import com.zfx.commonlib.network.result.NetworkResult
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.feature.coin.data.CoinData
import com.zfx.wanandroidcompose.feature.coin.data.PersonalCoinData
import com.zfx.wanandroidcompose.service.CoinService
import kotlinx.coroutines.flow.Flow

class CoinRepository : BaseRepository() {


    private val apiService by lazy {
        getApiService<CoinService>()
    }


    fun getCoinRank(page : Int) : Flow<NetworkResult<PageVO<CoinData>>>{
        return  requestFlow(
            apiCall = {
                apiService.getCoinRank(page)
            }
        )
    }


    fun getPersonalCoinList(page : Int) : Flow<NetworkResult<PageVO<PersonalCoinData>>>{
        return requestFlow(
            apiCall = {
                apiService.getPersonalCoinList(page)
            }
        )
    }


}