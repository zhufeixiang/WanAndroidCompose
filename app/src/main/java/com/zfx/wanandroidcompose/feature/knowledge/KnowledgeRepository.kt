package com.zfx.wanandroidcompose.feature.knowledge

import com.zfx.commonlib.network.extension.getApiService
import com.zfx.commonlib.network.repository.BaseRepository
import com.zfx.commonlib.network.result.NetworkResult
import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.KnowledgeItem
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.service.KnowledgeService
import kotlinx.coroutines.flow.Flow

class KnowledgeRepository : BaseRepository() {


    val apiService by lazy {
        getApiService<KnowledgeService>()
    }

    /**
     * 知识体系
     * */
    fun getKnowledgeTree() : Flow<NetworkResult<List<KnowledgeItem>>> {
        return requestFlow(
            apiCall = { apiService.getKnowledgeTree() },
        )
    }


    /**
     * 知识体系
     * */
    fun getArticleListById(page : Int,id : Int) : Flow<NetworkResult<PageVO<Article>>> {
        return requestFlow(
            apiCall = { apiService.getArticleListById(page,id) },
        )
    }



}