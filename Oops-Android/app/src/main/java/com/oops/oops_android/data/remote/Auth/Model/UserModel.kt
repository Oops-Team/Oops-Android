package com.oops.oops_android.data.remote.Auth.Model

import com.google.gson.annotations.SerializedName

/* Oops 로그인 & 회원 가입 API에서 사용하는 모델 */
data class OopsUserModel(
    @SerializedName("email") val email: String, // 이메일
    @SerializedName("password") val password: String? = null, // 비밀번호
    @SerializedName("name") val name: String? = null, // 닉네임
)

/* 카카오톡 & 구글 로그인 회원가입 API에서 사용하는 모델 */
data class ServerUserModel(
    @SerializedName("email") val email: String, // 이메일
    @SerializedName("name") val name: String? = null, // 닉네임
)