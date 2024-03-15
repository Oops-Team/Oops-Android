package com.oops.oops_android.data.remote.MyPage.Model

import com.google.gson.annotations.SerializedName

/* 회원 탈퇴 API에서 사용하는 모델 */
data class UserWithdrawalModel(
    @SerializedName("reason1") var reason1: Boolean, // 사유1
    @SerializedName("reason2") var reason2: Boolean, // 사유2
    @SerializedName("reason3") var reason3: Boolean, // 사유3
    @SerializedName("reason4") var reason4: Boolean, // 사유4
    @SerializedName("reason5") var reason5: Boolean, // 사유5
    @SerializedName("subReason") var subReason: String? = null, // 기타
)