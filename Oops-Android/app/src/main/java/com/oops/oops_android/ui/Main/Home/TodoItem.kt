package com.oops.oops_android.ui.Main.Home

import android.os.Parcelable
import android.widget.EditText
import android.widget.TextView
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

// 홈 화면의 오늘 할 일 목록
@Parcelize
data class TodoItem(
    var todoIdx: Long, // 일정 인덱스
    var todoName: String, // 이름
    var isComplete: Boolean // 완료 여부
): Parcelable

// 일정 수정화면에 넘겨 줄 오늘 할 일 리스트
@Parcelize
data class TodoListItem(
    var todoItem: ArrayList<TodoItem>, // 일정 아이템
    var date: LocalDate? = null, // 일정 날짜
    var todoTag: ArrayList<Int>? = null, // 태그 리스트
    var goOutTime: LocalTime? = null, // 외출 시간
    var remindTime: ArrayList<Int>? = null // 알림 시간 리스트
): Parcelable

// 일정 수정 화면에서 사용할 아이템
data class TodoCheckModifyItem(
    val id: Long? = null, // 일정 id
    val edt: EditText, // 일정 edittext
    val todoName: String? = null // 최초로 일정 등록 시 설정해둔 일정 이름
)

// 일정 수정 아이템
data class TodoModifyItem(
    val itemPos: Int, // list내의 아이템 position
    val edt: EditText, // edittext
    val tv: TextView // textview
)

// 일정 완료/미완료 수정 아이템
data class TodoCreateItem(
    val itemPos: Int, // list내의 아이템 position
    val isTodoComplete: Boolean // 변경된 일정 완료 여부
)