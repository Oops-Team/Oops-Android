package com.oops.oops_android.data.remote.Stuff.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Stuff.Model.InventoryChangeTodoModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffAddInventoryModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffDeleteHomeModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModifyHomeModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 소지품 화면에서 사용하는 API 연결 함수(홈, 인벤토리) */
class StuffService {
    private lateinit var stuffView: StuffView
    private lateinit var stuffObjectView: StuffObjectView
    private lateinit var commonView: CommonView

    fun setStuffView(stuffView: StuffView) {
        this.stuffView = stuffView
    }

    fun setStuffObjectView(stuffObjectView: StuffObjectView) {
        this.stuffObjectView = stuffObjectView
    }

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    // 소지품 목록 조회
    fun getStuffList(stuffModel: StuffModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.getStuffList(stuffModel).enqueue(object : Callback<StuffArrayResponse> {
            override fun onResponse(call: Call<StuffArrayResponse>, response: Response<StuffArrayResponse>) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StuffArrayResponse = response.body()!!
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

            override fun onFailure(call: Call<StuffArrayResponse>, t: Throwable) {
                Log.e("Stuff - Get Stuff List / FAILURE", t.stackTraceToString())
                stuffView.onGetStuffListFailure(-1, "")
            }
        })
    }

    // 인벤토리 내 소지품 추가
    fun addStuffList(inventoryIdx: Long, stuffAddInventoryModel: StuffAddInventoryModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.addStuffList(inventoryIdx, stuffAddInventoryModel).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Inventory", resp.data)
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

    // 인벤토리 내 소지품 수정
    fun modifyStuffList(inventoryIdx: Long, stuffAddInventoryModel: StuffAddInventoryModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.modifyStuffList(inventoryIdx, stuffAddInventoryModel).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Inventory", resp.data)
                }
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "인벤토리내 소지품 수정 실패")
                    commonView.onCommonSuccess(statusObject, messageObject) // 실패
                    Log.e("Stuff - Modify Stuff List / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Stuff - Modify Stuff List / FAILURE", t.stackTraceToString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 홈 화면의 챙겨야 할 것 수정(소지품 수정)
    fun modifyHomeStuff(stuffModifyHomeModel: StuffModifyHomeModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.modifyHomeStuff(stuffModifyHomeModel).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Home", stuffModifyHomeModel.isAddInventory)
                }
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "홈 화면의 소지품 수정 실패")
                    commonView.onCommonSuccess(statusObject, messageObject) // 실패
                    Log.e("Stuff - Modify Home Stuff / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Stuff - Modify Home Stuff / FAILURE", t.stackTraceToString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 홈 화면의 챙겨야 할 것 수정(소지품 삭제)
    fun deleteHomeStuff(deleteHomeModel: StuffDeleteHomeModel, position: Int) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.deleteHomeStuff(deleteHomeModel).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Delete Home Stuff", position)
                }
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "홈 화면의 챙겨야 할 것 수정(소지품 삭제) 실패")
                    commonView.onCommonSuccess(statusObject, messageObject) // 실패
                    Log.e("Stuff - Delete Home Stuff / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Stuff - Delete Home Stuff / FAILURE", t.stackTraceToString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경)
    fun changeTodoInventory(inventoryChangeTodoModel: InventoryChangeTodoModel) {
        val stuffService = retrofit.create(StuffInterface::class.java)
        stuffService.changeTodoInventory(inventoryChangeTodoModel).enqueue(object : Callback<StuffObjectResponse> {
            override fun onResponse(
                call: Call<StuffObjectResponse>,
                response: Response<StuffObjectResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StuffObjectResponse = response.body()!!
                    stuffObjectView.onGetInventoryListSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "인벤토리 목록 조회 실패")
                    stuffObjectView.onGetInventoryListFailure(statusObject, messageObject) // 실패
                    Log.e("Stuff - Get Inventory List / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<StuffObjectResponse>, t: Throwable) {
                Log.e("Stuff - Get Inventory List / FAILURE", t.stackTraceToString())
                stuffObjectView.onGetInventoryListFailure(-1, "")
            }
        })
    }
}