package com.oops.oops_android.data.remote.Sting.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 친구 화면에서 사용 */
class StingService {
    // 서비스 변수
    private lateinit var stingView: StingView

    fun setStingView(stingView: StingView) {
        this.stingView = stingView
    }

    // 외출 30분 전 친구 리스트 조회
    fun get30mFriends() {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.get30mFriends().enqueue(object : Callback<StingResponse> {
            override fun onResponse(call: Call<StingResponse>, response: Response<StingResponse>) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StingResponse = response.body()!!
                    stingView.onGet30mFriendsSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "외출 30분 전 친구 리스트 조회 실패")
                    stingView.onGet30mFriendsFailure(statusObject, messageObject) // 실패
                    Log.e("MyPage - Get 30m Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<StingResponse>, t: Throwable) {
                Log.e("Sting - Get 30m Friends / FAILURE", t.message.toString())
                stingView.onGet30mFriendsFailure(-1, "") // 실패
            }
        })
    }
}