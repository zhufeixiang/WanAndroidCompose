package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.Article
import com.zfx.wanandroidcompose.data.PageVO
import com.zfx.wanandroidcompose.data.WeChatAccount
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WechatService {


    /**
     * 公众号文章列表
     * */
    @GET("wxarticle/chapters/json")
    suspend fun getWechatList() : ApiResponse<List<WeChatAccount>>

    /**
     * 查看某个公众号历史数据
     * */
    @GET("wxarticle/list/{id}/{pageNum}/json")
    suspend fun getWechatListById(
        @Path("id") id: Int,
        @Path("pageNum") pageNum : Int) : ApiResponse<PageVO<Article>>

}