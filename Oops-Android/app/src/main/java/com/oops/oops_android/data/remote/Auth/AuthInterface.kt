package com.oops.oops_android.data.remote.Auth

import com.oops.oops_android.data.remote.Common.CommonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/* 로그인 및 회원가입 */
interface AuthInterface {
    // 닉네임 중복 검사
    @GET("/nickname/{name}")
    fun nicknameOverlap(
        @Path("name") name: String
    ): Call<CommonResponse>
}