package com.oops.oops_android.utils

import com.oops.oops_android.ApplicationClass.Companion.mSharedPreferences

// 사용자 닉네임 저장하기
fun saveNickname(nickname: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("Nickname", nickname)
    editor.apply()
}

// 사용자 닉네임 가져오기
fun getNickname(): String = mSharedPreferences.getString("Nickname", "").toString()