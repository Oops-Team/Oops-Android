package com.oops.oops_android.data.remote.MyPage.Api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/* 마이페이지 화면에서 사용하는 응답 목록 */
data class MyPageResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonObject
)
