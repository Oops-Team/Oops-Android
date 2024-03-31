package com.oops.oops_android.data.remote.Inventory.Model

import com.google.gson.annotations.SerializedName

/* 인벤토리 화면에서 사용하는 데이터 전송 모델 */
// 인벤토리 아이콘 변경
data class ChangeIconIdx(
    @SerializedName("inventoryIconIdx") val inventoryIconIdx: Int // new icon idx
)

// 인벤토리 생성, 수정
data class CreateInventory(
    @SerializedName("inventoryName") val inventoryName: String, // 인벤토리 이름
    @SerializedName("inventoryTag") val inventoryTag: ArrayList<Int> // 관련 태그
)