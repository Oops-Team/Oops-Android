package com.oops.oops_android.data.remote.Common

import com.google.gson.annotations.SerializedName

/* 대부분의 API에서 사용하는 공통 통신 모델 */
data class CommonResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: Any
)