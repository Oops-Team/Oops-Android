package com.oops.oops_android.data.remote.Auth.Api

import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Auth.Model.ServerUserModel
import com.oops.oops_android.data.remote.Common.CommonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/* 로그인 및 회원가입 */
interface AuthInterface {
    // Oops 로그인
    @POST("/user/login/{loginId}")
    fun oopsLogin(
        @Path("loginId") loginId: String,
        @Body user: OopsUserModel
    ): Call<CommonResponse>

    // 카카오톡 & 구글 로그인
    fun serverLogin(
        @Path("loginId") loginId: String,
        @Body user: ServerUserModel
    ): Call<CommonResponse>

    // 닉네임 중복 검사
    @GET("/user/nickname/{name}")
    fun nicknameOverlap(
        @Path("name") name: String
    ): Call<CommonResponse>

    // 이메일 중복 검사
    @GET("/user/email/{email}")
    fun emailOverlap(
        @Path("email") email: String
    ): Call<CommonResponse>

    // Oops 회원 가입
    @POST("/user/sign-up")
    fun oopsSignUp(
        @Body user: OopsUserModel
    ): Call<CommonResponse>
}