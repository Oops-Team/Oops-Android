package com.oops.oops_android.data.remote.Auth.Api

/* Oops 회원가입에서 사용하는 인터페이스 */
interface SignUpView {
    fun onSignUpSuccess(status: Int, message: String, data: Any? = null, isGetToken: Boolean) // 성공

    fun onSignUpFailure(status: Int, message: String) // 실패
}