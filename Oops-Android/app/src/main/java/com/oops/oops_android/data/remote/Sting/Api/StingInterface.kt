package com.oops.oops_android.data.remote.Sting.Api

import retrofit2.Call
import retrofit2.http.GET

/* 친구 화면에서 사용하는 API */
interface StingInterface {
    // 외출 30분 전 친구 리스트 조회
    @GET("/friends/sting")
    fun get30mFriends(
    ): Call<StingResponse>
}