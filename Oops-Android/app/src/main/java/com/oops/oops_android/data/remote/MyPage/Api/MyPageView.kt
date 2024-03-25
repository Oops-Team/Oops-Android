package com.oops.oops_android.data.remote.MyPage.Api

import com.google.gson.JsonObject

// 마이페이지 조회
interface MyPageView {
    fun onGetMyPageSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetMyPageFailure(status: Int, message: String) // 실패
}