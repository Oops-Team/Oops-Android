package com.oops.oops_android.data.remote.Todo.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Todo.Model.StuffDeleteModel
import com.oops.oops_android.data.remote.Todo.Model.TodoCompleteModel
import com.oops.oops_android.data.remote.Todo.Model.TodoCreateModel
import com.oops.oops_android.data.remote.Todo.Model.TodoDeleteAllModel
import com.oops.oops_android.data.remote.Todo.Model.TodoModifyModel
import com.oops.oops_android.data.remote.Todo.Model.TodoModifyNameModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate

/* 홈 화면 & 일정 화면 */
interface TodoInterface {
    /* 홈 화면 */
    // 일정 1개 조회
    @GET("/todo/detail/{date}")
    fun getTodo(
        @Path("date") date: LocalDate
    ): Call<TodoItemResponse>

    // 일정 전체(1달) 조회
    @GET("/todo/{date}")
    fun getMonthlyTodo(
        @Path("date") date: LocalDate
    ): Call<TodoItemResponse>

    // 일정 1개 이름 수정
    @PATCH("/todo/home/{todoIdx}")
    fun modifyTodoName(
        @Path("todoIdx") todoIdx: Long,
        @Body todoName: TodoModifyNameModel
    ): Call<CommonResponse>

    // 일정 1개 삭제
    @DELETE("/todo/home/{todoIdx}")
    fun deleteTodo(
        @Path("todoIdx") todoIdx: Long
    ): Call<CommonResponse>

    // 일정 완료/미완료 수정
    @PATCH("/todo/home/{todoIdx}/check")
    fun completeTodo(
        @Path("todoIdx") todoIdx: Long,
        @Body isTodoComplete: TodoCompleteModel
    ): Call<CommonResponse>

    // 소지품 1개 챙기기 완료
    @HTTP(method = "DELETE", path = "/todo/home/stuff", hasBody = true)
    fun deleteStuff(
        @Body stuffItem: StuffDeleteModel
    ): Call<CommonResponse>

    /* 일정 화면 */
    // 일정 추가
    @POST("/todo/create")
    fun createTodo(
        @Body todoItem: TodoCreateModel
    ): Call<CommonResponse>

    // 일정 수정
    @PATCH("/todo")
    fun modifyTodo(
        @Body todoModifyItem: TodoModifyModel
    ): Call<CommonResponse>

    // 일정 전체 삭제
    @HTTP(method = "DELETE", path = "/todo", hasBody = true)
    fun deleteAllTodo(
        @Body date: TodoDeleteAllModel
    ): Call<CommonResponse>
}