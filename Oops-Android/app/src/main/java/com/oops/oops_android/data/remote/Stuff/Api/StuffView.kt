package com.oops.oops_android.data.remote.Stuff.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject

// 소지품 목록 조회(홈, 인벤토리)
interface StuffView {
    fun onGetStuffListSuccess(status: Int, message: String, data: JsonArray? = null) // 성공

    fun onGetStuffListFailure(status: Int, message: String) // 실패
}

// 소지품 화면에서 인벤토리 조회
interface StuffObjectView {
    fun onGetInventoryListSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetInventoryListFailure(status: Int, message: String) // 실패
}