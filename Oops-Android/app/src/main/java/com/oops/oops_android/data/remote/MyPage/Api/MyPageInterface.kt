package com.oops.oops_android.data.remote.MyPage.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.MyPage.Model.UserPushAlertChangeModel
import com.oops.oops_android.data.remote.MyPage.Model.UserShowProfileChangeModel
import com.oops.oops_android.data.remote.MyPage.Model.UserWithdrawalModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

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

    // 프로필 사진 변경
    @Multipart
    @POST("/user/mypage/profile/image")
    fun changeProfile(
        @Part imageFile: MultipartBody.Part?
    ): Call<CommonResponse>

    // 프로필 공개
    @PATCH("/user/mypage/profile")
    fun showProfile(
        @Body isPublic: UserShowProfileChangeModel
    ): Call<CommonResponse>

    // 공지사항 조회
    @GET("/user/notices")
    fun getNotices(
    ): Call<NoticeResponse>

    // 푸시 알림
    @PATCH("/user/mypage/alert")
    fun setPushAlert(
        @Body isAlert: UserPushAlertChangeModel
    ): Call<CommonResponse>
}