package com.example.oops_android.ui.Main.Home

// 홈 화면의 오늘 할 일 목록
data class TodoItem(
    var todoIdx: Long, // 일정 인덱스
    var todoName: String, // 이름
    var isComplete: Boolean // 완료 여부
)
