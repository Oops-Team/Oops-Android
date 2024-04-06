package com.oops.oops_android.data.remote.Auth.Api

import android.util.Log
import com.oops.oops_android.ApplicationClass.Companion.retrofit
import com.oops.oops_android.data.remote.Auth.Model.ChangeOopsPwModel
import com.oops.oops_android.data.remote.Auth.Model.FindOopsPwModel
import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Auth.Model.ServerUserModel
import com.oops.oops_android.data.remote.Common.CommonObjectResponse
import com.oops.oops_android.data.remote.Common.CommonObjectView
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
    private lateinit var commonObjectView: CommonObjectView
    private lateinit var signUpView: SignUpView

    fun setCommonView(commonView: CommonView) {
        this.commonView = commonView
    }

    fun setCommonObjectView(commonObjectView: CommonObjectView) {
        this.commonObjectView = commonObjectView
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
                    // FCM 토큰이 여부 확인
                    val isGetToken = user.fcmToken != null

                    signUpView.onSignUpSuccess(resp.status, resp.message, resp.data, isGetToken)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Oops SignUp / ERROR", messageObject.toString())
                    signUpView.onSignUpFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Oops SignUp / FAILURE", t.message.toString())
                signUpView.onSignUpFailure(-1, "") // 실패
            }
        })
    }

    // 카카오톡, 구글 회원가입
    fun serverLogin(loginId: String, user: ServerUserModel) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.serverLogin(loginId, user).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    // FCM 토큰이 여부 확인
                    val isGetToken = user.fcmToken != null

                    signUpView.onSignUpSuccess(resp.status, resp.message, resp.data, isGetToken)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Server SignUp / ERROR", messageObject.toString())
                    signUpView.onSignUpFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Server SignUp / FAILURE", t.message.toString())
                signUpView.onSignUpFailure(-1, "") // 실패
            }
        })
    }

    // 이메일 찾기
    fun findOopsEmail(email: String) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.findOopsEmail(email).enqueue(object : Callback<CommonResponse> {
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
                    Log.e("AUTH - Find Email / ERROR", messageObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Find Email / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // 비밀번호 찾기 - 코드 전송
    fun findOopsPwCode(email: String) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.findOopsPwCode(email).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Code")
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Find Pw Code / ERROR", messageObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Find Pw Code / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }

    // 비밀번호 찾기 - 코드 인증
    fun findOopsPw(findOopsPwModel: FindOopsPwModel) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.findOopsPw(findOopsPwModel).enqueue(object : Callback<CommonObjectResponse> {
            override fun onResponse(
                call: Call<CommonObjectResponse>,
                response: Response<CommonObjectResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonObjectResponse = response.body()!!
                    commonObjectView.onCommonObjectSuccess(resp.status, resp.message, resp.data)
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Find Pw Code / ERROR", messageObject.toString())
                    commonObjectView.onCommonObjectFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonObjectResponse>, t: Throwable) {
                Log.e("AUTH - Find Pw / FAILURE", t.message.toString())
                commonObjectView.onCommonObjectFailure(-1, "") // 실패
            }
        })
    }

    // 새로운 비밀번호로 변경
    fun changeOopsPw(token: String, password: ChangeOopsPwModel) {
        val authService = retrofit.create(AuthInterface::class.java)
        authService.changeOopsPw(token, password).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                // 성공
                if (response.isSuccessful) {
                    val resp: CommonResponse = response.body()!!
                    commonView.onCommonSuccess(resp.status, "Change")
                }
                // 실패
                else {
                    val jsonObject = JSONObject(response.errorBody()?.string().toString())
                    val statusObject = jsonObject.getInt("status")
                    val messageObject = jsonObject.optString("message", "")
                    Log.e("AUTH - Change Pwd / ERROR", messageObject.toString())
                    commonView.onCommonFailure(statusObject, messageObject)
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("AUTH - Change Pwd / FAILURE", t.message.toString())
                commonView.onCommonFailure(-1, "") // 실패
            }
        })
    }
}