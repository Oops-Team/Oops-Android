package com.oops.oops_android.ui.Main.Inventory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/* 인벤토리 화면의 카테고리 아이템 */
// UI에서 쓰일 데이터
@Parcelize
data class CategoryItemUI(
    var inventoryIdx: Long, // 인벤토리 idx
    var inventoryIconIdx: Int, // 인벤토리 아이콘
    var inventoryName: String, // 인벤토리 이름
    var isSelected: Boolean = false, // 인벤토리가 현재 선택되어 있는지 대한 여부
    var inventoryTag: ArrayList<Int>? = null // 인벤토리 태그 리스트
) : Parcelable

// 전체 인벤토리 리스트
@Parcelize
class CategoryList: ArrayList<CategoryItemUI>(), Parcelable
