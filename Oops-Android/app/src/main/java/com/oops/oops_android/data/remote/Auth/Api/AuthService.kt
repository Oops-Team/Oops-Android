package com.oops.oops_android.data.remote.Auth.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Common.CommonResponse
import com.oops.oops_android.data.remote.Common.CommonView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 로그인 & 회원가입 & 아이디찾기/비밀번호 찾기 뷰에서 사용 */
class AuthService {
    // 서비스 변수
    private lateinit var commonView: CommonView
    private lateinit var signUpView: SignUpView

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    // 닉네임 중복 검사
    fun nicknameOverlap(name: String) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.nicknameOverlap(name).enqueue(object : Callback<CommonResponse> {
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
                    val messageObject = jsonObject.optString("message", "닉네임 중복 확인 중 오류가 발생했습니다")
                    Log.e("AUTH - Overlap Name / ERROR", jsonObject.toString())
                    when (statusObject) {
                        409 -> commonView.onCommonFailure(statusObject, messageObject) // 닉네임 중복인 경우
                        else -> commonView.onCommonFailure(statusObject, messageObject)
                    }
                }
            }

            // 네트워크 연결에 실패한 경우
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Overlap Name / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // Oops 로그인
    fun oopsLogin(loginId: String, user: OopsUserModel) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.oopsLogin(loginId, user).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Oops Login / ERROR", messageObject.toString())
                    when (statusObject) {
                        // 이메일 및 비밀번호 불일치 오류
                        404, 400 -> commonView.onCommonFailure(statusObject, messageObject)
                        else -> commonView.onCommonFailure(statusObject, messageObject)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Oops Login / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // 이메일 중복 검사
    fun emailOverlap(email: String) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.emailOverlap(email).enqueue(object : Callback<CommonResponse> {
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
                    val messageObject = jsonObject.optString("message", "이미 사용 중인 이메일이에요")
                    Log.e("AUTH - Overlap Email / ERROR", jsonObject.toString())
                    when (statusObject) {
                        409 -> commonView.onCommonFailure(statusObject, messageObject) // 닉네임 중복인 경우
                        else -> commonView.onCommonFailure(statusObject, messageObject)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Overlap Email / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // Oops 회원가입
    fun oopsSignUp(user: OopsUserModel) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.oopsSignUp(user).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    signUpView.onSignUpSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Oops SignUp / ERROR", messageObject.toString())
                    when (statusObject) {
                        else -> signUpView.onSignUpFailure(statusObject, messageObject)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Oops SignUp / FAILURE", t.message.toString())
                signUpView.onSignUpFailure(-1, "") // 실패
            }
        })
    }
}