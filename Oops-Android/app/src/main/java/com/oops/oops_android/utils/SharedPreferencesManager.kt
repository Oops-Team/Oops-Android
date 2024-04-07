package com.oops.oops_android.utils

import com.oops.oops_android.ApplicationClass.Companion.X_AUTH_TOKEN
import com.oops.oops_android.ApplicationClass.Companion.mSharedPreferences

// 최신 사용자 닉네임 저장하기
fun saveNickname(nickname: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("Nickname", nickname)
    editor.apply()
}

// 최신 사용자 닉네임 가져오기
fun getNickname(): String = mSharedPreferences.getString("Nickname", "").toString()

// 최신 사용자 로그인 유형 저장하기
fun saveLoginId(loginId: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("LoginId", loginId)
    editor.apply()
}

// 최신 사용자 로그인 유형 가져오기
fun getLoginId(): String = mSharedPreferences.getString("LoginId", "").toString()

// 최신 사용자 로그인 유형 삭제
fun removeLoginId() {
    val editor = mSharedPreferences.edit()
    editor.remove("LoginId")
    editor.apply()
}

// 토큰 저장하기
fun saveToken(token: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_AUTH_TOKEN, token)
    editor.apply()
}

// 토큰 가져오기
fun getToken(): String? = mSharedPreferences.getString(X_AUTH_TOKEN, null)

// 토큰 초기화
fun removeToken() {
    val editor = mSharedPreferences.edit()
    editor.remove(X_AUTH_TOKEN)
    editor.apply()
}

// 모든 값 초기화
fun clearSpf() {
    val editor = mSharedPreferences.edit()
    editor.clear().apply()
}