package com.oops.oops_android.data.remote.Todo.Api

import com.google.gson.JsonObject

interface TodoView {
    // 일정 1개 조회
    fun onGetTodoSuccess(status: Int, message: String, data: JsonObject? = null) // 성공

    fun onGetTodoFailure(status: Int, message: String) // 실패
}