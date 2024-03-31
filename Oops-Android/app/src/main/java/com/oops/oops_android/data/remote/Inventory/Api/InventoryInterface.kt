package com.oops.oops_android.data.remote.Inventory.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Inventory.Model.ChangeIconIdx
import com.oops.oops_android.data.remote.Inventory.Model.CreateInventory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    // 인벤토리 아이콘 변경
    @PATCH("/inventories/{inventoryIdx}/icon")
    fun changeInventoryIcon(
        @Path("inventoryIdx") inventoryIdx: Long,
        @Body inventoryIconIdx: ChangeIconIdx
    ): Call<CommonResponse>

    // 인벤토리 생성
    @POST("/inventories/create")
    fun createInventory(
        @Body createInventory: CreateInventory
    ): Call<CommonResponse>

    // 인벤토리 수정
    @PATCH("/inventories/{inventoryIdx}")
    fun modifyInventory(
        @Path("inventoryIdx") inventoryIdx: Long,
        @Body modifyInventory: CreateInventory
    ): Call<CommonResponse>

    // 인벤토리 삭제
    @DELETE("/inventories/{inventoryIdx}")
    fun deleteInventory(
        @Path("inventoryIdx") inventoryIdx: Long
    ): Call<CommonResponse>
}