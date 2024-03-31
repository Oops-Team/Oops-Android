package com.oops.oops_android.data.remote.Stuff.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Stuff.Model.StuffAddInventoryModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 소지품 화면에서 사용하는 API 연결 함수(홈, 인벤토리) */
class StuffService {
    private lateinit var stuffView: StuffView
    private lateinit var commonView: CommonView

    fun setStuffView(stuffView: StuffView) {
        this.stuffView = stuffView
    }

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
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

    // 인벤토리 내 소지품 추가
    fun addStuffList(inventoryIdx: Long, stuffAddInventoryModel: StuffAddInventoryModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.addStuffList(inventoryIdx, stuffAddInventoryModel).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Add Stuff", resp.data)
                }
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "인벤토리내 소지품 추가 실패")
                    commonView.onCommonSuccess(statusObject, messageObject) // 실패
                    Log.e("Stuff - Add Stuff List / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Stuff - Add Stuff List / FAILURE", t.stackTraceToString())
               commonView.onCommonFailure(-1, "")
            }
        })
    }
}