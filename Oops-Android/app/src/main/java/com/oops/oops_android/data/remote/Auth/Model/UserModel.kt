package com.oops.oops_android.data.remote.Auth.Model

import com.google.gson.annotations.SerializedName

/* Oops 로그인 & 회원 가입 API에서 사용하는 모델 */
data class OopsUserModel(
    @SerializedName("email") val email: String, // 이메일
    @SerializedName("password") val password: String? = null, // 비밀번호
    @SerializedName("name") val name: String? = null, // 닉네임
    @SerializedName("fcmToken") val fcmToken: String? = null, // firebase 토큰
    @SerializedName("isAlert") val isAlert: Boolean? = null // 알림 설정 여부
)

/* 카카오톡 & 구글 로그인 회원가입 API에서 사용하는 모델 */
data class ServerUserModel(
    @SerializedName("email") val email: String, // 이메일
    @SerializedName("name") val name: String? = null, // 닉네임
    @SerializedName("fcmToken") val fcmToken: String? = null, // firebase 토큰
    @SerializedName("isAlert") val isAlert: Boolean? = null // 알림 설정 여부
)

/* 비밀번호 찾기 - 코드 인증 API에서 사용하는 모델 */
data class FindOopsPwModel(
    @SerializedName("code") val code: String, // 인증 코드
    @SerializedName("email") val email: String // 사용자 이메일
)

/* 새로운 비밀번호로 변경 API에서 사용하는 모델 */
data class ChangeOopsPwModel(
    @SerializedName("password") val password: String // 비밀번호
)