package com.oops.oops_android.data.remote.Inventory.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

/* 인벤토리 화면에서 사용하는 API 연결 함수 */
class InventoryService {
    private lateinit var inventoryView: InventoryView

    fun setInventoryView(inventoryView: InventoryView) {
        this.inventoryView = inventoryView
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
                    Log.e("Inventory - Get All Inventory", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<InventoryObjectResponse>, t: Throwable) {
                Log.e("Inventory - Get All Inventory", t.message.toString())
                inventoryView.onGetInventoryFailure(-1, "")
            }
        })
    }
}