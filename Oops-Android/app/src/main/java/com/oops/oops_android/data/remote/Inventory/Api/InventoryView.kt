package com.oops.oops_android.data.remote.Inventory.Api

import com.google.gson.JsonObject

/* 인벤토리 전체 조회 */
interface InventoryView {
    fun onGetInventorySuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetInventoryFailure(status: Int, message: String) // 실패
}