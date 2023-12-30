package com.example.oops_android.ui.Main.Home

// 홈 화면의 주간 캘린더에 들어갈 아이템
data class CalendarIWeeklytem(
    var day: String,  // 요일
    var date: String, // 날짜
    var fullDate: String, // 년도-날짜-요일
    var isSelected: Boolean,  // 선택된 날짜
    var isToday: Boolean // 오늘 날짜
)
