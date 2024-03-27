package com.oops.oops_android.data.remote.MyPage.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.MyPage.Model.UserShowProfileChangeModel
import com.oops.oops_android.data.remote.MyPage.Model.UserWithdrawalModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH

/* 마이페이지 화면에서 사용하는 API */
interface MyPageInterface {
    // 마이페이지 조회
    @GET("/user/mypage")
    fun getMyPage(
    ): Call<MyPageResponse>

    // 탈퇴
    @HTTP(method = "DELETE", path = "/user", hasBody = true)
    fun oopsWithdrawal(
        @Body reasonItem: UserWithdrawalModel
    ): Call<CommonResponse>

    // 프로필 공개
    @PATCH("/user/mypage/profile")
    fun showProfile(
        @Body isPublic: UserShowProfileChangeModel
    ): Call<CommonResponse>
}