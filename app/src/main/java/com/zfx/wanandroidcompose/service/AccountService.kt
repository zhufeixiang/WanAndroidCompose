package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.AccountBody
import com.zfx.wanandroidcompose.data.User
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountService {

    /**
     * 登录
     * 成功时返回用户信息，失败时返回错误信息（无data）
     * */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun signIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<User>


    /**
     * 注册
     * */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResponse<User>


    /**
     * 退出
     * */
    @GET("logout/json")
    suspend fun signOut(): ApiResponse<String>

}