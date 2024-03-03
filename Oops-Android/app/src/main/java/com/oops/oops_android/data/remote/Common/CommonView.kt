package com.oops.oops_android.data.remote.Common

/* 대부분의 뷰에서 처리할 공통 인터페이스 */
interface CommonView {
    fun onCommonSuccess(status: Int, message: String) // 성공

    fun onCommonFailure(status: Int, message: String) // 실패
}