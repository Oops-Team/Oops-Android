package com.example.oops_android.ui.Main.Home

import java.time.LocalDate

// 홈 화면의 챙겨야 할 것 아이템
data class StuffItem(
    var stuffIdx: Long, // 소지품 idx
    var stuffImgUrl: Int, // 소지품 이미지
    var stuffName: String, // 소지품 이름
    var date: LocalDate? = null, // 날짜
    var isTakeStuff: Boolean? = null // 챙김 여부(0: 안 챙김, 1: 챙김)
)
