package com.oops.oops_android.data.remote.Stuff.Api

import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/* 소지품 화면에서 사용하는 API 모델 */
interface StuffInterface {
    // 소지품 목록 조회(홈, 인벤토리)
    @POST("/todo/stuff")
    fun getStuffList(
        @Body stuffModel: StuffModel
    ): Call<StuffResponse>
}