package com.oops.oops_android.data.remote.Inventory.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Inventory.Model.ChangeIconIdx
import com.oops.oops_android.data.remote.Inventory.Model.CreateInventory
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

/* 인벤토리 화면에서 사용하는 API 연결 함수 */
class InventoryService {
    private lateinit var inventoryView: InventoryView
    private lateinit var commonView: CommonView

    fun setInventoryView(inventoryView: InventoryView) {
        this.inventoryView = inventoryView
    }

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    // 전체 인벤토리 조회
    fun getAllInventory() {
        val inventoryService = retrofit.create(InventoryInterface::class.java)
        inventoryService.getAllInventory().enqueue(object : Callback<InventoryObjectResponse>{
            override fun onResponse(
                call: Call<InventoryObjectResponse>,
                response: Response<InventoryObjectResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: InventoryObjectResponse = response.body()!!
                    inventoryView.onGetInventorySuccess(resp.status, "All Inventory", resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "전체 인벤토리 조회 실패")
                    inventoryView.onGetInventoryFailure(statusObject, messageObject)
                    Log.e("Inventory - Get All Inventory / FAILURE", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<InventoryObjectResponse>, t: Throwable) {
                Log.e("Inventory - Get All Inventory / ERROR", t.message.toString())
                inventoryView.onGetInventoryFailure(-1, "")
            }
        })
    }

    // 상세 인벤토리 조회
    fun getDetailInventory(inventoryIdx: Long) {
        val inventoryService = retrofit.create(InventoryInterface::class.java)
        inventoryService.getDetailInventory(inventoryIdx).enqueue(object : Callback<InventoryObjectResponse>{
            override fun onResponse(
                call: Call<InventoryObjectResponse>,
                response: Response<InventoryObjectResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: InventoryObjectResponse = response.body()!!
                    inventoryView.onGetInventorySuccess(resp.status, "Detail Inventory", resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "상세 인벤토리 조회 실패")
                    inventoryView.onGetInventoryFailure(statusObject, messageObject)
                    Log.e("Inventory - Get Detail Inventory / FAILURE", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<InventoryObjectResponse>, t: Throwable) {
                Log.e("Inventory - Get Detail Inventory / ERROR", t.message.toString())
                inventoryView.onGetInventoryFailure(-1, "")
            }
        })
    }

    // 인벤토리 아이콘 변경
    fun changeInventoryIcon(inventoryIdx: Long, inventoryIconIdx: ChangeIconIdx) {
        val inventoryService = retrofit.create(InventoryInterface::class.java)
        inventoryService.changeInventoryIcon(inventoryIdx, inventoryIconIdx).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Change Icon", resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "인벤토리 아이콘 변경 실패")
                    commonView.onCommonFailure(statusObject, messageObject)
                    Log.e("Inventory - Change Icon / FAILURE", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Inventory - Change Icon / ERROR", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }

    // 인벤토리 생성
    fun createInventory(createInventory: CreateInventory) {
        val inventoryService = retrofit.create(InventoryInterface::class.java)
        inventoryService.createInventory(createInventory).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Create Inventory", resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "인벤토리 생성 실패")
                    commonView.onCommonFailure(statusObject, messageObject)
                    Log.e("Inventory - Create Inventory / FAILURE", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Inventory - Create Inventory / ERROR", t.message.toString())
                commonView.onCommonFailure(-1, "")
            }
        })
    }
}