package com.zfx.wanandroidcompose.feature.square

import com.zfx.commonlib.network.extension.getApiService
import com.zfx.commonlib.network.repository.BaseRepository
import com.zfx.commonlib.network.result.NetworkResult
import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.service.SquareService
import kotlinx.coroutines.flow.Flow

class SquareRepository : BaseRepository() {


    val apiService by lazy {
        getApiService<SquareService>()
    }



    fun getSquareArticles(page : Int) : Flow<NetworkResult<PageVO<Article>>> {
        return  requestFlow(
            apiCall = {
                apiService.getSquareArticles(page)
            }
        )
    }



}