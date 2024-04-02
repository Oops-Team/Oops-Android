package com.oops.oops_android.data.remote.Stuff.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/* 소지품 화면에서 사용하는 응답 목록(배열) */
data class StuffArrayResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonArray? = null
)

/* 소지품 화면에서 사용하는 응답 목록(배열) */
data class StuffObjectResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonObject? = null
)
