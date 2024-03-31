package com.oops.oops_android.data.remote.Inventory.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/* 인벤토리 화면에서 사용하는 API */
interface InventoryInterface {
    // 인벤토리 전체 조회
    @GET("/inventories")
    fun getAllInventory(
    ): Call<InventoryObjectResponse>

    // 인벤토리 상세 조회
    @GET("/inventories/{inventoryIdx}")
    fun getDetailInventory(
        @Path("inventoryIdx") inventoryIdx: Long
    ): Call<InventoryObjectResponse>
}