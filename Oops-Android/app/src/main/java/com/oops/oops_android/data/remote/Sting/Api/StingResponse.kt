package com.oops.oops_android.data.remote.Sting.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/* 친구 화면에서 사용하는 응답 목록(배열) */
data class StingResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonArray? = null
)

/* 친구 화면에서 사용하는 응답 목록2 */
data class StingObjectResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonObject? = null
)
