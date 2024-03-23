package com.oops.oops_android.ui.Main.Home

/* 홈 화면에서 사용하는 인벤토리 리스트 */
data class HomeInventoryItem(
    var inventoryId: Long, // 인벤토리 idx
    var inventoryName: String, // 인벤토리 이름
    var inventoryIconIdx: Int, // 인벤토리 아이콘
    var isInventoryUsed: Boolean // 현재 사용 중인 인벤토리
)