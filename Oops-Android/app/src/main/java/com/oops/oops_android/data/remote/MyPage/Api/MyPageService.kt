package com.oops.oops_android.data.remote.MyPage.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 마이페이지 뷰에서 사용 */
class MyPageService {
    // 서비스 변수
    private lateinit var commonView: CommonView

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    // 프로필 공개
    fun showProfile(isPublic: Boolean) {
        val myPageService = retrofit.create(MyPageInterface::class.java)
        myPageService.showProfile(isPublic).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, resp.message)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "프로필 상태 전환 실패")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("MyPage - Show Profile / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("MyPage - Show Profile / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }
}