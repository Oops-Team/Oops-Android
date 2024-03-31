package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 소지품 추가&수정 시 인벤토리 화면에서 넘겨줄 데이터
@Parcelize
data class StuffItemUI(
    val inventoryId: Long
): Parcelable

// 소지품 추가 화면에서 띄울 소지품 아이템
data class StuffAddItem(
    var stuffImgUrl: String, // 소지품 이미지
    var stuffName: String, // 소지품 이름
    var isSelected: Boolean = false // 선택 여부
)