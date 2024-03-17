package com.oops.oops_android.ui.Main.Home

import java.time.LocalDate
import java.time.LocalTime

// 홈 화면의 오늘 할 일 목록
data class TodoItem(
    var todoIdx: Long, // 일정 인덱스
    var todoName: String, // 이름
    var isComplete: Boolean // 완료 여부
)

// 일정 수정화면에 넘겨 줄 오늘 할 일 리스트
data class TodoListItem(
    var todoItem: ArrayList<TodoItem>, // 일정 아이템
    var date: LocalDate? = null, // 일정 날짜
    var todoTag: ArrayList<Int>? = null, // 태그 리스트
    var goOutTime: LocalTime? = null, // 외출 시간
    var remindTime: ArrayList<Int>? = null // 알림 시간 리스트
)