package com.oops.oops_android.data.remote.Sting.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/* 외출 30분 전 친구 리스트 조회 */
interface StingView {
    fun onGet30mFriendsSuccess(status: Int, message: String, data: JsonArray? = null) // 성공

    fun onGet30mFriendsFailure(status: Int, message: String) // 실패
}

/* 사용자 리스트 조회 */
interface UsersView {
    fun onGetUsersSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetUsersFailure(status: Int, message: String) // 실패
}