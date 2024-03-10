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

// 토큰 저장하기
fun saveToken(token: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_AUTH_TOKEN, token)
    editor.apply()
}

// 토큰 가져오기
fun getToken(): String? = mSharedPreferences.getString(X_AUTH_TOKEN, null)

// 토큰 초기화
fun clearToken() {
    val editor = mSharedPreferences.edit()
    editor.clear().apply()
}