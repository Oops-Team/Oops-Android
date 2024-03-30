package com.oops.oops_android.data.remote.MyPage.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject

// 마이페이지 조회
interface MyPageView {
    fun onGetMyPageSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetMyPageFailure(status: Int, message: String) // 실패
}

// 공지사항 조회
interface NoticeView {
    fun onGetNoticeSuccess(status: Int, message: String, data: JsonArray) // 성공
    fun onGetNoticeFailure(status: Int, message: String) // 실패
}