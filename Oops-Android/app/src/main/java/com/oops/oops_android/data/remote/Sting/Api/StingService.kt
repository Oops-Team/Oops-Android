package com.oops.oops_android.data.remote.Sting.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Sting.Model.StingFriendIdModel
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import com.oops.oops_android.ui.Main.Sting.FriendsItem
import com.oops.oops_android.ui.Main.Sting.StingAcceptModel
import com.oops.oops_android.ui.Main.Sting.StingRefuseModel
import com.oops.oops_android.ui.Main.Sting.StingRequestModel
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
    fun stingFriend(stingFriendModel: StingFriendModel) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.stingFriend(stingFriendModel).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Sting Friends", stingFriendModel.name)
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
    fun requestFriends(name: StingFriendModel, position: Int) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.requestFriends(name).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Request Friends", StingRequestModel(name.name, position))
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

    // 친구 수락
    fun acceptFriends(friendId: StingFriendIdModel, position: Int, newFriend: FriendsItem) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.acceptFriends(friendId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Accept Friends", StingAcceptModel(position, newFriend))
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "Accept Friends")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Accept Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Sting - Accept Friends / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // 친구 끊기&거절
    fun refuseFriends(
        friendId: StingFriendIdModel,
        isRefuse: Boolean,
        position: Int,
        newFriend: FriendsItem
    ) {
        val stingService = retrofit.create(StingInterface::class.java)
        stingService.refuseFriends(friendId).enqueue(object : Callback<CommonResponse>{
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Refuse Friends", StingRefuseModel(isRefuse, position, newFriend))
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "Refuse Friends")
                    commonView.onCommonFailure(statusObject, messageObject) // 실패
                    Log.e("Sting - Refuse Friends / ERROR", "$jsonObject $messageObject")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("Sting - Refuse Friends / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }
}