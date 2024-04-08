package com.oops.oops_android.data.remote.Common

import com.google.gson.JsonObject

/* 대부분의 뷰에서 처리할 공통 인터페이스 */
interface CommonView {
    fun onCommonSuccess(status: Int, message: String, data: Any? = null) // 성공

    fun onCommonFailure(status: Int, message: String, data: String? = null) // 실패
}

interface CommonObjectView {
    fun onCommonObjectSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onCommonObjectFailure(status: Int, message: String) // 실패
}