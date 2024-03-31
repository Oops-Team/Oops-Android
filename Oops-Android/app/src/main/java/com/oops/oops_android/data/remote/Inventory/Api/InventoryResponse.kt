package com.oops.oops_android.data.remote.Inventory.Api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/* 인벤토리 화면에서 사용하는 응답 */
data class InventoryObjectResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonObject
)
