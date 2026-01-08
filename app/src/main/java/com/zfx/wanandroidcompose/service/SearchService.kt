package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.HotKey
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SearchService {

    /**
     * 搜索文章
     * @param page 页码，从0开始
     * @param keyword 搜索关键词
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun getSearchArticles(
        @Path("page") page: Int,
        @Field("k") keyword: String
    ): ApiResponse<PageVO<Article>>


    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getSearchHotKey(): ApiResponse<List<HotKey>>


}