package com.example.oops_android.ui.Main.Home

import java.time.LocalDate

// 월간 캘린더에 dot을 그리기 위해서 필요한 정보
data class MonthlyItem (
    val date: LocalDate, // 날짜
    val isComplete: Boolean // 일부완료/완료x: 0, 일정 완료o: 1
)