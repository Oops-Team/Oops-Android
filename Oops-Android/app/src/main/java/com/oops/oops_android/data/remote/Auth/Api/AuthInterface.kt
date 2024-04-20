package com.oops.oops_android.data.remote.Auth.Api

import com.oops.oops_android.data.remote.Auth.Model.ChangeOopsPwModel
import com.oops.oops_android.data.remote.Auth.Model.FindOopsPwModel
import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Auth.Model.ServerUserModel
import com.oops.oops_android.data.remote.Common.CommonObjectResponse
import com.oops.oops_android.data.remote.Common.CommonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/* 로그인 및 회원가입 */
interface AuthInterface {
    // Oops 로그인
    @POST("/user/login/{loginId}")
    fun oopsLogin(
        @Path("loginId") loginId: String,
        @Body user: OopsUserModel
    ): Call<CommonObjectResponse>

    // 구글 로그인
    @POST("/user/login/{loginId}")
    fun googleLogin(
        @Path("loginId") loginId: String,
        @Body user: ServerUserModel
    ): Call<CommonObjectResponse>

    // 네이버 로그인 - 1 (재로그인, 회원가입 - 1)
    @POST("/user/login/{loginId}")
    fun naverLogin(
        @Path("loginId") loginId: String,
        @Body user: ServerUserModel
    ): Call<CommonObjectResponse>

    // 네이버 로그인 - 2 (회원가입 - 2)
    @POST("/user/sign-up/naver")
    fun naverSignUp(
        @Body user: ServerUserModel
    ): Call<CommonObjectResponse>

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
    ): Call<CommonObjectResponse>

    // 이메일 찾기
    @GET("/user/find/email/{email}")
    fun findOopsEmail(
        @Path("email") email: String
    ): Call<CommonResponse>

    // 비밀번호 찾기 - 코드 전송
    @GET("/user/find/password/{email}")
    fun findOopsPwCode(
        @Path("email") email: String
    ): Call<CommonResponse>

    // 비밀번호 찾기 - 코드 인증
    @POST("/user/find/password")
    fun findOopsPw(
        @Body findOopsPwModel: FindOopsPwModel
    ): Call<CommonObjectResponse>

    // 새로운 비밀번호로 변경
    @PATCH("user/change/password")
    fun changeOopsPw(
        @Header("tempToken") token: String,
        @Body password: ChangeOopsPwModel
    ): Call<CommonResponse>
}