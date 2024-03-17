package com.oops.oops_android.data.remote.Todo.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Todo.Model.StuffDeleteModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/* 홈 화면 & 일정 화면 뷰에서 사용 */
class TodoService {
    // 서비스 변수
    private lateinit var todoView: TodoView
    private lateinit var commonView: CommonView

    fun setTodoView(todoView: TodoView) {
        this.todoView = todoView
    }

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    // 일정 1개 조회
    fun getTodo(date: LocalDate) {
        val todoService = retrofit.create(TodoInterface::class.java)
        todoService.getTodo(date).enqueue(object : Callback<TodoItemResponse> {
            override fun onResponse(
                call: Call<TodoItemResponse>,
                response: Response<TodoItemResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: TodoItemResponse = response.body()!!
                    todoView.onGetTodoSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "일정 1개 조회 실패")
                    Log.e("TODO - Get Todo / ERROR", jsonObject.toString())
                    todoView.onGetTodoFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<TodoItemResponse>, t: Throwable) {
                Log.e("TODO - Get Todo / FAILURE", t.message.toString())
                todoView.onGetTodoFailure(-1, "") // 실패
            }
        })
    }

    // 일정 1개 이름 수정
    fun modifyTodoName(todoIdx: Long, todoName: String) {
        val todoService = retrofit.create(TodoInterface::class.java)
        todoService.modifyTodoName(todoIdx, todoName).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Todo Modify")
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "일정 1개 수정 실패")
                    Log.e("TODO - Modify Todo Name / ERROR", jsonObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("TODO - Modify Todo Name / Failure", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 일정 1개 삭제
    fun deleteTodo(todoIdx: Long) {
        val todoService = retrofit.create(TodoInterface::class.java)
        todoService.deleteTodo(todoIdx).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Todo Delete")
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "일정 1개 삭제 실패")
                    Log.e("TODO - Delete Todo Name / ERROR", jsonObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("TODO - Delete Todo / Failure", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 일정 완료/미완료 수정
    fun completeTodo(todoIdx: Long, isTodoComplete: Boolean) {
        val todoService = retrofit.create(TodoInterface::class.java)
        todoService.completeTodo(todoIdx, isTodoComplete).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Todo Complete")
                }
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "일정 완료/미완료 수정 실패")
                    Log.e("TODO - Delete Todo Complete / ERROR", jsonObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("TODO - Delete Todo Complete / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 소지품 1개 삭제(소지품 챙기기 완료)
    fun deleteStuff(stuffItem: StuffDeleteModel) {
        val todoService = retrofit.create(TodoInterface::class.java)
        todoService.deleteStuff(stuffItem).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Stuff Delete")
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "소지품 1개 챙김 여부 삭제 실패")
                    Log.e("TODO - Delete Stuff / ERROR", jsonObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("TODO - Delete Stuff / Failure", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }
}