package com.zfx.wanandroidcompose.service

import com.zfx.wanandroidcompose.data.NavigationJson
import com.zfx.wanandroidcompose.network.ApiResponse
import retrofit2.http.GET

interface NavigationService {


    @GET("navi/json")
    suspend fun getNavigationJson() : ApiResponse<List<NavigationJson>>
}