package com.oops.oops_android.data.remote.Todo.Api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.LocalDate

// 일정 1개 조회
interface TodoView {
    fun onGetTodoSuccess(status: Int, message: String, data: JsonObject? = null, todoDate: LocalDate? = null) // 성공

    fun onGetTodoFailure(status: Int, message: String) // 실패
}

// 일정 전체(1달) 조회
interface TodoMonthlyView {
    fun onGetMonthlyTodoSuccess(status: Int, message: String, data: JsonArray? = null) // 성공

    fun onGetMonthlyTodoFailure(status: Int, message: String) // 실패
}