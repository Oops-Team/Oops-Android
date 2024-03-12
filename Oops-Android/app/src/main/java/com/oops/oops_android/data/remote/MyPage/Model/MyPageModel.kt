package com.oops.oops_android.data.remote.MyPage.Model

import com.google.gson.annotations.SerializedName

/* 회원 탈퇴 API에서 사용하는 모델 */
data class UserWithdrawalModel(
    @SerializedName("reason1") val reason1: Boolean = false, // 사유1
    @SerializedName("reason2") val reason2: Boolean = false, // 사유2
    @SerializedName("reason3") val reason3: Boolean = false, // 사유3
    @SerializedName("reason4") val reason4: Boolean = false, // 사유4
    @SerializedName("reason5") val reason5: Boolean = false, // 사유5
    @SerializedName("subReason") var subReason: String? = null, // 기타
)