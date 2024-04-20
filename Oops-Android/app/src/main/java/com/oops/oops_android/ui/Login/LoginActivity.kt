package com.oops.oops_android.ui.Login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.db.Entity.User
import com.oops.oops_android.data.remote.Auth.Api.AuthService
import com.oops.oops_android.data.remote.Auth.Api.SignUpView
import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Auth.Model.ServerUserModel
import com.oops.oops_android.data.remote.Common.CommonObjectView
import com.oops.oops_android.databinding.ActivityLoginBinding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Main.MainActivity
import com.oops.oops_android.utils.AlarmUtils.setAllAlarm
import com.oops.oops_android.utils.AlarmUtils.setCancelAllAlarm
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.CustomPasswordTransformationMethod
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveLoginId
import com.oops.oops_android.utils.saveNickname
import com.oops.oops_android.utils.saveToken
import com.oops.oops_android.utils.setOnSingleClickListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime

/* 로그인 화면(최초 진입 화면) */
class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate), SignUpView, CommonObjectView {

    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수
    private var isEmailValid: Boolean = false // 이메일 유효성 검사
    private var isPwdValid: Boolean = false // 비밀번호 유효성 검사
    private var naverEmail: String? = null // 네이버 이메일

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // status bar 숨기기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 화면 터치 시 키보드 숨기기
        binding.login.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 비밀번호 mask의 메소드(스타일) 지정
        binding.edtLoginPwd.transformationMethod = CustomPasswordTransformationMethod()

        // 비밀번호 mask on/off 이벤트
        binding.iBtnLoginToggle.setOnClickListener {
            isPwdMask =
                if (isPwdMask) {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnLoginToggle, isPwdMask, binding.edtLoginPwd)
                    false
                } else {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnLoginToggle, isPwdMask, binding.edtLoginPwd)
                    true
                }
        }

        // 이메일 입력 이벤트
        binding.edtLoginEmail.onTextChanged {
            // alert 숨기기
            binding.tvLoginEmailAlert.visibility = View.GONE
            // 이메일 형식이 맞는지 확인
            isEmailValid = EditTextUtils.emailRegex(binding.edtLoginEmail.text.toString().trim())

            // 모든 유효성 확인
            checkValid()
        }

        // 비밀번호 입력 이벤트
        binding.edtLoginPwd.onTextChanged {
            // alert 숨기기
            binding.tvLoginPwdAlert.visibility = View.GONE

            // 비밀번호 유효성 확인
            isPwdValid = binding.edtLoginPwd.text.toString().isNotEmpty()

            // 모든 유효성 확인
            checkValid()
        }

        // 로그인 버튼 클릭 이벤트
        binding.btnLoginConfirm.setOnSingleClickListener {
            if (isEmailValid && isPwdValid) {
                // FCM 토큰 가져오기 및 Oops 회원가입 API 연동
                getFCMToken("oops")
            }
        }

        // ID/PW 찾기 버튼 클릭 이벤트
        binding.tvLoginIdPwFind.setOnClickListener {
            startNextActivity(FindAccountActivity::class.java)
        }

        // 회원가입 버튼 클릭 이벤트
        binding.tvLoginSignup.setOnClickListener {
            startNextActivity(SignUpActivity::class.java)
        }

        // 네이버 로그인 버튼 클릭 이벤트
        binding.iBtnLoginNaver.setOnSingleClickListener {
            naverLogin()
        }

        // 구글 로그인 버튼 클릭 이벤트
        binding.iBtnLoginGoogle.setOnSingleClickListener {
            // TODO: 추후 구글 로그인 API 승인 허가가 떨어지면 코드 수정하기
            //googleLogin()
        }

        // 최근 로그인한 플랫폼에 따른 말풍선 띄우기
        try {
            val userDB = AppDatabase.getUserDB()
            val loginId = userDB?.userDao()?.getLoginId()
            if (loginId == "naver") {
                binding.lLayoutLoginRecentNaver.visibility = View.VISIBLE
                binding.lLayoutLoginRecentGoogle.visibility = View.GONE
            }
            else if (loginId == "google") {
                binding.lLayoutLoginRecentGoogle.visibility = View.VISIBLE
                binding.lLayoutLoginRecentNaver.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.e("LoginActivity - LoginId", e.stackTraceToString())
        }
    }

    // 이메일, 비밀번호 유효성 검사
    private fun checkValid() {
        if (isEmailValid && isPwdValid) {
            // 로그인 버튼 색상 전환
            binding.btnLoginConfirm.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Main_500))
            binding.btnLoginConfirm.setTextColor(getColor(R.color.White))
        }
        else {
            binding.btnLoginConfirm.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Gray_100))
            binding.btnLoginConfirm.setTextColor(getColor(R.color.Gray_400))
        }
    }

    // FCM 토큰 가져오기 및 각 로그인 API 연결 (getFCMToken함수를 호출하면 다음 함수가 실행됨)
    override fun connectOopsAPI(token: String?, loginId: String?) {
        try {
            when (loginId) {
                // oops 로그인 이라면
                "oops" -> {
                    val authService = AuthService()
                    authService.setCommonObjectView(this)
                    authService.oopsLogin(
                        loginId,
                        OopsUserModel(
                            binding.edtLoginEmail.text.toString(),
                            binding.edtLoginPwd.text.toString(),
                            null,
                            token
                        )
                    )
                }

                // 네이버 로그인 이라면
                "naver" -> {
                    // 네이버 로그인 - 1 API 연결
                    val authService = AuthService()
                    authService.setCommonObjectView(this)
                    authService.naverLogin(
                        loginId,
                        ServerUserModel(
                            naverEmail.toString(), // 이메일
                            null,
                            token
                        )
                    )
                }

                // todo: 수정 필요
                // 구글 로그인 이라면
                "google" -> {
                    // 기존에 로그인했던 계정 확인하기
                    val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

                    // 최신 로그인 유형 저장
                    saveLoginId("google")

                    // Oops 서버 로그인 api 연결
                    val authService = AuthService()
                    authService.setSignUpView(this@LoginActivity)
                    authService.googleLogin(
                        loginId,
                        ServerUserModel(
                            googleSignInAccount!!.email.toString(), // 이메일
                            null,
                            token
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("LoginActivity", "로그인 오류")
        }
    }

    // oops 로그인 API, 네이버 로그인 - 1 API 연결 성공
    override fun onCommonObjectSuccess(status: Int, message: String, data: JsonObject?) {
        when (status) {
            200 -> {
                when (message) {
                    // oops 로그인이라면
                    "oops" -> {
                        try {
                            // json 파싱
                            val jsonObject = JSONObject(data.toString())

                            // xAuthToken 저장
                            val xAuthToken: String = jsonObject.getString("xAuthToken").toString()
                            saveToken(xAuthToken)

                            // 사용자 닉네임 저장
                            val name: String = jsonObject.getString("name").toString()
                            saveNickname(name)

                            // 최신 로그인 유형 저장
                            saveLoginId("oops")

                            // room db에 최신 유저 정보 저장
                            val userDB = AppDatabase.getUserDB()!!
                            userDB.userDao().deleteAllUser() // db값 삭제
                            userDB.userDao().insertUser(User("oops", name))

                            // 기존에 저장되어 있던 모든 알람 삭제(db, 알람 취소)
                            setCancelAllAlarm(this)

                            // 알람 리스트 저장
                            val alertList = jsonObject.getJSONArray("alertList")
                            for (i in 0 until alertList.length()) {
                                val subObject = alertList.getJSONObject(i)
                                val dateObject = subObject.getString("date")
                                val outTimeObject = subObject.getString("outTime")

                                val date = LocalDate.parse(dateObject.toString()) // 날짜
                                val goOutTime = LocalTime.parse(outTimeObject.toString()) // 외출 시간

                                val tempRemindTime: JSONArray = subObject.getJSONArray("remindList")
                                val remindList = ArrayList<Int>() // 알림 시간 리스트
                                for (j in 0 until tempRemindTime.length()) {
                                    remindList.add(tempRemindTime[j] as Int) // 알림 시간 리스트에 정보 저장
                                }

                                // 알람 등록하기
                                setAllAlarm(this, date, goOutTime.hour, goOutTime.minute, remindList)
                            }

                            // 홈 화면으로 이동
                            startActivityWithClear(MainActivity::class.java)

                        } catch (e: JSONException) {
                            Log.e("LoginActivity - Oops Login", e.stackTraceToString())
                            showToast(getString(R.string.toast_server_error))
                        }
                    }

                    // 네이버 로그인이라면(재로그인의 경우)
                    "naver" -> {
                        try {
                            // json 파싱
                            val jsonObject = JSONObject(data.toString())

                            // xAuthToken 저장
                            val xAuthToken: String = jsonObject.getString("xAuthToken").toString()
                            saveToken(xAuthToken)

                            // 사용자 닉네임 저장
                            val name: String = jsonObject.getString("name").toString()
                            saveNickname(name)

                            // 최신 로그인 유형 저장
                            saveLoginId("naver")

                            // room db에 최신 유저 정보 저장
                            val userDB = AppDatabase.getUserDB()!!
                            userDB.userDao().deleteAllUser() // db값 삭제
                            userDB.userDao().insertUser(User("naver", name))

                            // 기존에 저장되어 있던 모든 알람 삭제(db, 알람 취소)
                            setCancelAllAlarm(this)

                            // 알람 리스트 저장
                            val alertList = jsonObject.getJSONArray("alertList")
                            for (i in 0 until alertList.length()) {
                                val subObject = alertList.getJSONObject(i)
                                val dateObject = subObject.getString("date")
                                val outTimeObject = subObject.getString("outTime")

                                val date = LocalDate.parse(dateObject.toString()) // 날짜
                                val goOutTime = LocalTime.parse(outTimeObject.toString()) // 외출 시간

                                val tempRemindTime: JSONArray = subObject.getJSONArray("remindList")
                                val remindList = ArrayList<Int>() // 알림 시간 리스트
                                for (j in 0 until tempRemindTime.length()) {
                                    remindList.add(tempRemindTime[j] as Int) // 알림 시간 리스트에 정보 저장
                                }

                                // 알람 등록하기
                                setAllAlarm(this, date, goOutTime.hour, goOutTime.minute, remindList)
                            }

                            // 홈 화면으로 이동
                            startActivityWithClear(MainActivity::class.java)

                        } catch (e: JSONException) {
                            Log.e("LoginActivity - Naver Login", e.stackTraceToString())
                            showToast(getString(R.string.toast_server_error))
                        }
                    }

                    // 네이버 최초 회원가입이라면
                    "회원가입을 진행해주시기 바랍니다" -> {
                        // 닉네임 설정 화면으로 이동
                        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)

                        // 다음 화면에서 room db에 값 저장 및 회원가입을 위해 해당 값 전달
                        intent.putExtra("LoginId", "naver")
                        intent.putExtra("Email", naverEmail)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    // oops 로그인 API, 네이버 로그인 - 1 API 연결 실패
    override fun onCommonObjectFailure(status: Int, message: String) {
        when (message) {
            "oops" -> {
                when (status) {
                    // 이메일 불일치
                    404 -> {
                        binding.tvLoginEmailAlert.visibility = View.VISIBLE
                        isEmailValid = false
                    }
                    // 비밀번호 불일치
                    400 -> {
                        binding.tvLoginPwdAlert.visibility = View.VISIBLE
                        isPwdValid = false
                    }
                    // 그 외
                    else -> showToast(getString(R.string.toast_server_error))
                }

                // 로그인 버튼 활성화 해제
                checkValid()
            }
            else -> {
                showToast(getString(R.string.toast_server_error))
            }
        }
    }

    /* --- SNS 로직 관련 로직 --- */

    /* 네이버 로그인 관련 로직 */
    // 네이버 로그인 서버 API 연결 성공
    private fun naverLogin() {
        val oAuthLoginCallback = object : OAuthLoginCallback {
            // 인증 성공
            override fun onSuccess() {
                // 네이버api에서 프로필 정보 가져오기
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    // 호출 성공
                    override fun onSuccess(result: NidProfileResponse) {

                        // 이메일 값 저장
                        naverEmail = result.profile?.email.toString()

                        // 네이버 로그인 API 연결
                        getFCMToken("naver")
                    }

                    // 호출 실패
                    override fun onFailure(httpStatus: Int, message: String) {
                    }

                    // 호출 오류
                    override fun onError(errorCode: Int, message: String) {
                    }
                })
            }

            // 인증 실패
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("LoginActivity - Naver", "errorCode : $errorCode errorDescription: $errorDescription")
                showToast(getString(R.string.toast_login_naver_failure))
            }

            // 인증 오류
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
    }

    /* 구글 로그인 관련 로직 */
    // 구글 로그인 서버 API 연결
    private fun googleLogin() {
        val gso = googleSignIn()

        // 객체 생성
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 기존에 로그인했던 계정 확인하기
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

        // 기존에 로그인 했었다면
        if (googleSignInAccount != null) {
            getFCMToken("google")
        }
        // 신규 회원 가입이라면
        else {
            val signInIntent = googleSignInClient.signInIntent
            getResult.launch(signInIntent)
        }
    }

    // 구글 로그인 api 연결
    private fun googleSignIn(): GoogleSignInOptions {
        // 구글 로그인 옵션
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_login_client_id))
            .requestEmail()
            .build()
    }

    // result 객체
    private var getResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(task)
        } else {
            showToast("구글 로그인이 취소되었습니다.")
            Log.e("구글 로그인 getResult resultCode", it.resultCode.toString()) // 0 : cancel, 1: user, -1 : ok
            Log.e("구글 로그인", GoogleSignIn.getSignedInAccountFromIntent(it.data).exception.toString())
        }
    }

    // 구글 로그인 - 토큰 요청
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val googleSignInAccount = completedTask.getResult(ApiException::class.java)
            // 이메일 값 가져오기
            if (googleSignInAccount != null) {
                val email = googleSignInAccount.email

                // 닉네임 설정 화면으로 이동
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                // 다음 화면에서 room db에 값 저장하기 위해 해당 값 전달
                intent.putExtra("LoginId", "google")
                intent.putExtra("Email", email)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }

        } catch (e: ApiException) {
            Log.e("구글 로그인 토큰 요청 실패", e.statusCode.toString())
        }
    }

    // TODO: 수정 필요
    // 구글 로그인 성공
    override fun onSignUpSuccess(status: Int, message: String, data: JsonObject?, isGetToken: Boolean) {
        when (status) {
            // 성공
            200 -> {
                try {
                    // json 파싱
                    val jsonObject = JSONObject(data.toString())

                    // xAuthToken 저장
                    val xAuthToken: String = jsonObject.getString("xAuthToken").toString()
                    saveToken(xAuthToken)

                    // 사용자 닉네임 저장
                    val name: String = jsonObject.getString("name").toString()
                    saveNickname(name)

                    val userDB = AppDatabase.getUserDB() // room db의 user db
                    userDB?.userDao()?.updateUserName(
                        name,
                        "naver"
                    )

                    // 홈 화면으로 이동
                    startActivityWithClear(MainActivity::class.java)

                } catch (e: JSONException) {
                    Log.e("LoginActivity - Server Login", e.stackTraceToString())
                    showToast(getString(R.string.toast_server_error))
                }
            }
        }
    }

    // TODO: 수정 필요
    // 구글 로그인 실패
    override fun onSignUpFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}