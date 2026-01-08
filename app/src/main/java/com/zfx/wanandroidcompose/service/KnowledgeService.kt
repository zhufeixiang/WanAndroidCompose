package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.KnowledgeItem
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 知识体系相关的api
 * */
interface KnowledgeService {
    
    /**
     * 获取知识体系树
     * @return 知识体系列表（树形结构）
     */
    @GET("tree/json")
    suspend fun getKnowledgeTree(): ApiResponse<List<KnowledgeItem>>


    /**
     * 知识体系下的文章
     * */
    @GET("article/list/{page}/json")
    suspend fun getArticleListById(
        @Path("page") page: Int,
        @Query("cid") id: Int): ApiResponse<PageVO<Article>>
}