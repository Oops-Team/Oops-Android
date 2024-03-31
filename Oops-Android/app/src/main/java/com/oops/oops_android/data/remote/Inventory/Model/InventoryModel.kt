package com.oops.oops_android.data.remote.Inventory.Model

import com.google.gson.annotations.SerializedName

/* 인벤토리 화면에서 사용하는 데이터 전송 모델 */
data class ChangeIconIdx(
    @SerializedName("inventoryIconIdx") val inventoryIconIdx: Int // new icon idx
)
