package com.oops.oops_android.data.remote.MyPage.Model

import com.google.gson.annotations.SerializedName

/* 마이페이지 화면에서 사용하는 모델 */
// 프로필 공개/비공개 변경 모델
data class UserShowProfileChangeModel(
    @SerializedName("isPublic") var isPublic: Boolean, // 프로필 공개 여부
)

// 회원 탈퇴 API에서 사용하는 모델
data class UserWithdrawalModel(
    @SerializedName("reasonType") var reasonType: Int, // 사유
    @SerializedName("subReason") var subReason: String? = null, // 기타
)