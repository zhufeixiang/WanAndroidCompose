package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.data.WeChatAccount
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * author : zhufeixiang
 * date : 2026/1/24
 * des :
 */
interface ProgramService {

    //项目分类
    @GET("project/tree/json")
    suspend fun getProgramTree() : ApiResponse<List<WeChatAccount>>

    //项目列表数据
    @GET("project/list/{page}/json")
    suspend fun getProgramsById(
        @Path("page") page : Int,
        @Query("cid") cid : Int
    ) : ApiResponse<PageVO<Article>>
}