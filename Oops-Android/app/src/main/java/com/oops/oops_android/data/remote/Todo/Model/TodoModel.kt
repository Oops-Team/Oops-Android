package com.oops.oops_android.data.remote.Todo.Model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

/* 홈 화면 & 일정 화면에서 사용하는 데이터 모델 */
/* 홈 화면 */
// 일정 1개 이름 수정
data class TodoModifyNameModel(
    @SerializedName("todoName") var todoName: String // 일정 이름
)

// 일정 완료/미완료 수정
data class TodoCompleteModel(
    @SerializedName("isTodoComplete") var isTodoComplete: Boolean // 일정 완료/미완료 여부
)

// 소지품 1개 삭제(챙김 완료) 데이터 모델
data class StuffDeleteModel(
    @SerializedName("date") var date: LocalDate, // 일정 날짜
    @SerializedName("stuffName") var stuffName: String // 소지품 이름
)

/* 일정 화면 */
// 일정 추가 데이터 모델
data class TodoCreateModel(
    @SerializedName("date") var date: String, // 일정 날짜
    @SerializedName("todoName") var todoName: ArrayList<String>, // 일정 리스트
    @SerializedName("todoTag") var todoTag: ArrayList<Int>, // 관련 태그 리스트
    @SerializedName("goOutTime") var goOutTime: String, // 외출 시간
    @SerializedName("remindTime") var remindTime: ArrayList<Int> // 알림 시간
)