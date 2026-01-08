package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SquareService {


    /**
     * 广场列表数据
     * */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareArticles(@Path("page") page : Int): ApiResponse<PageVO<Article>>


}