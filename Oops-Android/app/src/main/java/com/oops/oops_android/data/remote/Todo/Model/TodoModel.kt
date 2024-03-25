package com.oops.oops_android.data.remote.Todo.Model

import com.google.gson.annotations.SerializedName
import com.oops.oops_android.ui.Main.Home.TodoModifyItem
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

// 일정 수정 데이터 모델
data class TodoModifyModel(
    @SerializedName("date") var date: String, // 일정 날짜
    @SerializedName("todoTag") var todoTag: ArrayList<Int>, // 관련 태그 리스트
    @SerializedName("goOutTime") var goOutTime: String, // 외출 시간
    @SerializedName("remindTime") var remindTime: ArrayList<Int>, // 알림 시간
    @SerializedName("deleteTodoIdx") var deleteTodoIdx: ArrayList<Long>? = null, // 삭제한 일정들의 아이디값
    @SerializedName("modifyTodo") var modifyTodo: ArrayList<TodoModifyItem2>? = null, // 수정한 일정의 리스트
    @SerializedName("addTodoName") var addTodoName: ArrayList<String>? = null // 추가한 일정의 이름 목록
)

// 수정한 일정들의 데이터 모델
data class TodoModifyItem2(
    @SerializedName("todoIdx") var todoIdx: Long, // idx
    @SerializedName("todoName") var todoName: String // 일정 이름
)

// 일정 전체 삭제 데이터 모델
data class TodoDeleteAllModel(
    @SerializedName("date") var date: String, // 일정 날짜
)