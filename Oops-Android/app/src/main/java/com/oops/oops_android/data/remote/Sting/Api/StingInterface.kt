package com.oops.oops_android.data.remote.Sting.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Sting.Model.StingFriendIdModel
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/* 친구 화면에서 사용하는 API */
interface StingInterface {
    // 외출 30분 전 친구 리스트 조회
    @GET("/friends/sting")
    fun get30mFriends(
    ): Call<StingResponse>

    // 콕콕 찌르기
    @POST("/friends/sting")
    fun stingFriend(
        @Body name: StingFriendModel
    ): Call<CommonResponse>

    // 사용자 리스트 조회
    @GET("/friends/search/{name}")
    fun getUsers(
        @Path("name") name: String
    ): Call<StingObjectResponse>

    // 친구 리스트 조회
    @GET("/friends")
    fun getFriends(
    ): Call<StingResponse>

    // 친구 신청
    @POST("/friends/request")
    fun requestFriends(
        @Body name: StingFriendModel
    ): Call<CommonResponse>

    // 친구 수락
    @PATCH("/friends/accept")
    fun acceptFriends(
        @Body friendId: StingFriendIdModel
    ): Call<CommonResponse>

    // 친구 끊기 & 거절
    @DELETE("/friends")
    fun refuseFriends(
        @Body friendId: StingFriendIdModel
    ): Call<CommonResponse>
}