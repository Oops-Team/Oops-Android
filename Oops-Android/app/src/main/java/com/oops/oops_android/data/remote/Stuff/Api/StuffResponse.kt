package com.oops.oops_android.data.remote.Stuff.Api

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

/* 친구 화면에서 사용하는 응답 목록(배열) */
data class StuffResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonArray? = null
)