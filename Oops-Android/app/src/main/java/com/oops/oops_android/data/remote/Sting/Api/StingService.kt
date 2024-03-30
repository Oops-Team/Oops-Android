package com.oops.oops_android.data.remote.Sting.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 친구 화면에서 사용 */
class StingService {
    // 서비스 변수
    private lateinit var stingView: StingView
    private lateinit var usersView: UsersView
    private lateinit var commonView: CommonView

    fun setStingView(stingView: StingView) {
        this.stingView = stingView
    }

    fun setUsersView(usersView: UsersView) {
        this.usersView = usersView
    }

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    // 외출 30분 전 친구 리스트 조회
    fun get30mFriends() {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.get30mFriends().enqueue(object : Callback<StingResponse> {
            override fun onResponse(call: Call<StingResponse>, response: Response<StingResponse>) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StingResponse = response.body()!!
                    stingView.onGetFriendsSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "외출 30분 전 친구 리스트 조회 실패")
                    stingView.onGetFriendsFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Get 30m Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<StingResponse>, t: Throwable) {
                Log.e("Sting - Get 30m Friends / FAILURE", t.message.toString())
                stingView.onGetFriendsFailure(-1, "") // 실패
            }
        })
    }

    // 콕콕 찌르기
    fun stingFriend(name: StingFriendModel) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.stingFriend(name).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, resp.message, name.toString())
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "콕콕 찌르기 실패")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Get Sting Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Sting - Get Sting Friend / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // 사용자 리스트 조회
    fun getUsers(name: String) {
        if (name.isNotBlank()) {
            val stingService = retrofit.create(StingInterface::class.java)
            stingService.getUsers(name).enqueue(object : Callback<StingObjectResponse>{
                override fun onResponse(
                    call: Call<StingObjectResponse>,
                    response: Response<StingObjectResponse>
                ) {
                    // 성공
                    if (response.isSuccessful) {
                        val resp: StingObjectResponse = response.body()!!
                        usersView.onGetUsersSuccess(resp.status, resp.message, resp.data)
                    }
                    // 실패
                    else {
                        val jsonObject = JSONObject(response.errorBody()?.string().toString())
                        val statusObject = jsonObject.getInt("status")
                        val messageObject = jsonObject.optString("message", "사용자 리스트 조회 실패")
                        usersView.onGetUsersFailure(statusObject, messageObject) // 실패
                        Log.e("Sting - Get Users / ERROR", "$jsonObject $messageObject")
                    }
                }

                override fun onFailure(call: Call<StingObjectResponse>, t: Throwable) {
                    Log.e("Sting - Get Users / FAILURE", t.message.toString())
                    usersView.onGetUsersFailure(-1, "") // 실패
                }
            })
        }
    }

    // 친구 리스트 조회
    fun getFriends() {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.getFriends().enqueue(object : Callback<StingResponse>{
            override fun onResponse(call: Call<StingResponse>, response: Response<StingResponse>) {
                // 성공
                if (response.isSuccessful) {
                    val resp: StingResponse = response.body()!!
                    stingView.onGetFriendsSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "친구 리스트 조회 실패")
                    stingView.onGetFriendsFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Get Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<StingResponse>, t: Throwable) {
                Log.e("Sting - Get Friends / FAILURE", t.message.toString())
                stingView.onGetFriendsFailure(-1, "") // 실패
            }
        })
    }

    // 친구 신청
    fun requestFriends(name: StingFriendModel) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.requestFriends(name).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, resp.message, name.toString())
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "Request Friends")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Request Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Sting - Request / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }
}