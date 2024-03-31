package com.oops.oops_android.data.remote.Stuff.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/* 소지품 화면에서 사용하는 API 데이터 모델 */
data class StuffModel(
    @SerializedName("date") val date: LocalDate? = null, // 일정의 날짜값(홈 화면에서 사용하는 경우)
    @SerializedName("inventoryId") val inventoryId: Long? = null // 인벤토리 아이디(인벤토리 화면에서 사용하는 경우)
)
