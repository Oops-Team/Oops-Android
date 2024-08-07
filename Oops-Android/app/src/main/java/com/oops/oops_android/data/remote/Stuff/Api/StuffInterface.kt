package com.oops.oops_android.data.remote.Stuff.Api

import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Stuff.Model.InventoryChangeTodoModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffAddInventoryModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffDeleteHomeModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModifyHomeModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/* 소지품 화면에서 사용하는 API 모델 */
interface StuffInterface {
    // 소지품 목록 조회(홈, 인벤토리)
    @POST("/todo/stuff")
    fun getStuffList(
        @Body stuffModel: StuffModel
    ): Call<StuffArrayResponse>

    // 인벤토리 내 소지품 추가
    @POST("/inventories/{inventoryIdx}/stuff")
    fun addStuffList(
        @Path("inventoryIdx") inventoryIdx: Long,
        @Body stuffName: StuffAddInventoryModel
    ): Call<CommonResponse>

    // 인벤토리 내 소지품 수정
    @PATCH("/inventories/{inventoryIdx}/stuff")
    fun modifyStuffList(
        @Path("inventoryIdx") inventoryIdx: Long,
        @Body stuffName: StuffAddInventoryModel
    ): Call<CommonResponse>

    // 홈 화면의 챙겨야 할 것 수정(소지품 수정)
    @PATCH("/todo/stuff")
    fun modifyHomeStuff(
        @Body stuffModifyHomeModel: StuffModifyHomeModel
    ): Call<CommonResponse>

    // 홈 화면의 챙겨야 할 것 수정(소지품 삭제)
    @HTTP(method = "DELETE", path = "/todo/stuff", hasBody = true)
    fun deleteHomeStuff(
        @Body deleteHomeModel: StuffDeleteHomeModel
    ): Call<CommonResponse>

    // 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경)
    @PATCH("/todo/inventories/select")
    fun changeTodoInventory(
        @Body inventoryChangeTodoModel: InventoryChangeTodoModel
    ): Call<StuffObjectResponse>
}