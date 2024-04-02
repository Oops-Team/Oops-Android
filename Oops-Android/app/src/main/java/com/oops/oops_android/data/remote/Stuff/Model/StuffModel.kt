package com.oops.oops_android.data.remote.Stuff.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/* 소지품 화면에서 사용하는 API 데이터 모델 */
// 소지품 목록 조회
data class StuffModel(
    @SerializedName("date") val date: String? = null, // 일정의 날짜값(홈 화면에서 사용하는 경우)
    @SerializedName("inventoryId") val inventoryId: Long? = null // 인벤토리 아이디(인벤토리 화면에서 사용하는 경우)
)

// 인벤토리 내 소지품 추가
data class StuffAddInventoryModel(
    @SerializedName("stuffName") val stuffName: ArrayList<String> // 소지품 이름 리스트
)

// 챙겨야 할 것 수정(소지품 삭제)
data class StuffDeleteHomeModel(
    @SerializedName("date") val date: String, // 일정 날짜
    @SerializedName("stuffName") val stuffName: String // 소지품 이름
)

// 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경)
data class InventoryChangeTodoModel(
    @SerializedName("date") val date: String? = null, // 일정 날짜
    @SerializedName("inventoryName") val inventoryName: String // 인벤토리 이름
)