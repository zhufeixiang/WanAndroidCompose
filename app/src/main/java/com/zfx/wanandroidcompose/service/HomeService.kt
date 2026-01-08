package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.BannerItem
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 首页的相关的api
 * */
interface HomeService {

    @GET("banner/json")
    suspend fun getBanner() : ApiResponse<List<BannerItem>>

    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page : Int) : ApiResponse<PageVO<Article>>

    /**
     *置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticle() : ApiResponse<List<Article>>
    

}