package com.oops.oops_android.data.remote.Stuff.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 소지품 화면에서 사용하는 API 연결 함수(홈, 인벤토리) */
class StuffService {
    private lateinit var stuffView: StuffView

    fun setStuffView(stuffView: StuffView) {
        this.stuffView = stuffView
    }

    // 소지품 목록 조회
    fun getStuffList(stuffModel: StuffModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.getStuffList(stuffModel).enqueue(object : Callback<StuffResponse> {
            override fun onResponse(call: Call<StuffResponse>, response: Response<StuffResponse>) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StuffResponse = response.body()!!
                    stuffView.onGetStuffListSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "소지품 목록 조회 실패")
                    stuffView.onGetStuffListFailure(statusObject, messageObject) // 실패
                    Log.e("Stuff - Get Stuff List / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<StuffResponse>, t: Throwable) {
                Log.e("Stuff - Get Stuff List / FAILURE", t.stackTraceToString())
                stuffView.onGetStuffListFailure(-1, "")
            }
        })
    }
}