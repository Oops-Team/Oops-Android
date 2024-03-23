package com.oops.oops_android.data.remote.Todo.Api

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/* 홈 화면 & 일정 화면에서 사용하는 응답 목록 */

// 일정 1개 조회 & 일정 1달 조회 응답 리스트
data class TodoItemResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: JsonElement
)