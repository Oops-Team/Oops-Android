package com.example.oops_android.ui.Tutorial

import java.time.LocalDate

// 튜토리얼3 화면의 소지품 아이템
data class TutorialStuffItem (
    var stuffImgUrl: Int, // 소지품 이미지
    var stuffName: String, // 소지품 이름
    var isSelected: Boolean = false // 선택 여부
)
