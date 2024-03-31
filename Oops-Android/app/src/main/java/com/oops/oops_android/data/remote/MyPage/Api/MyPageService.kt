package com.oops.oops_android.data.remote.MyPage.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Model.UserShowProfileChangeModel
import com.oops.oops_android.data.remote.MyPage.Model.UserWithdrawalModel
import com.oops.oops_android.ui.Main.MyPage.WithdrawalItem
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 마이페이지 뷰에서 사용 */
class MyPageService {
    // 서비스 변수
    private lateinit var commonView: CommonView
    private lateinit var myPageView: MyPageView
    private lateinit var noticeView: NoticeView

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    fun setMyPageView(myPageView: MyPageView) {
        this.myPageView = myPageView
    }

    fun setNoticeView(noticeView: NoticeView) {
        this.noticeView = noticeView
    }

    // 마이페이지 조회
    fun getMyPage() {
        val myPageService = retrofit.create(MyPageInterface::class.java)
        myPageService.getMyPage().enqueue(object : Callback<MyPageResponse> {
            override fun onResponse(
                call: Call<MyPageResponse>,
                response: Response<MyPageResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: MyPageResponse = response.body()!!
                    myPageView.onGetMyPageSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "마이페이지 조회 실패")
                    myPageView.onGetMyPageFailure(statusObject, messageObject) // 실패
                    Log.e("MyPage - Get MyPage / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                Log.e("MyPage - Get MyPage / FAILURE", t.message.toString())
                myPageView.onGetMyPageFailure(-1, "") // 실패
            }
        })
    }

    // 프로필 공개
    fun showProfile(isPublic: UserShowProfileChangeModel) {
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

    // 공지사항 조회
    fun getNotices() {
        val myPageService = retrofit.create(MyPageInterface::class.java)
        myPageService.getNotices().enqueue(object : Callback<NoticeResponse>{
            override fun onResponse(
                call: Call<NoticeResponse>,
                response: Response<NoticeResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: NoticeResponse = response.body()!!
                    noticeView.onGetNoticeSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "공지사항 조회 실패")
                    noticeView.onGetNoticeFailure(statusObject, messageObject) // 실패
                    Log.e("MyPage - Get Notices / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                Log.e("MyPage - Get Notices / FAILURE", t.message.toString())
                noticeView.onGetNoticeFailure(-1, "") // 실패
            }
        })
    }

    // 회원 탈퇴
    fun oopsWithdrawal(userWithdrawalModel: UserWithdrawalModel) {
        val myPageService = retrofit.create(MyPageInterface::class.java)
        myPageService.oopsWithdrawal(userWithdrawalModel).enqueue(object : Callback<CommonResponse>{
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
                    val messageObject = jsonObject.optString("message", "회원 탈퇴 실패")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("MyPage - Oops Withdrawal / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("MyPage - Oops Withdrawal / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }
}