package com.oops.oops_android.data.remote.Todo.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/* 홈 화면 & 일정 화면에서 사용하는 데이터 모델 */
// 소지품 1개 삭제(챙김 완료) 데이터 모델
data class StuffDeleteModel(
    @SerializedName("date") var date: LocalDate, // 일정 날짜
    @SerializedName("stuffName") var stuffName: String // 소지품 이름
)