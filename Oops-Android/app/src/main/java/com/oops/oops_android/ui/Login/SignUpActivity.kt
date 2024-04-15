package com.oops.oops_android.ui.Login

import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.db.Entity.User
import com.oops.oops_android.data.remote.Auth.Api.AuthService
import com.oops.oops_android.data.remote.Auth.Api.SignUpView
import com.oops.oops_android.data.remote.Auth.Model.ServerUserModel
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.databinding.ActivitySignUpBinding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Tutorial.TutorialActivity
import com.oops.oops_android.utils.AlarmUtils.setCancelAllAlarm
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveLoginId
import com.oops.oops_android.utils.saveNickname
import com.oops.oops_android.utils.saveToken
import com.oops.oops_android.utils.setOnSingleClickListener
import org.json.JSONObject
import java.lang.Exception

/* 회원가입 - 닉네임 입력 화면 */
class SignUpActivity: BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate), CommonView, SignUpView {

    // 어떤 버튼 클릭 이벤트로 화면을 넘어왔는지 파악하기 위한 변수
    private var loginId: String? = null

    // 네이버 & 구글의 이메일 값
    private var serverEmail: String? = null

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // 어떤 버튼 클릭 이벤트로 화면을 넘어 왔는지 파악한 후 값 적용하기
        try {
            loginId = intent.getStringExtra("LoginId")
            serverEmail = intent.getStringExtra("Email")
        } catch (e: Exception) {
            Log.e("SignUpActivity - intent", e.stackTraceToString())
        }

        // 화면 터치 시 키보드 숨기기
        binding.cLayoutSignUpTop.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 중복 확인 버튼 클릭 이벤트
        binding.tvSignUpOverlapBtn.setOnSingleClickListener {
            val alert = binding.tvSignUpAlert
            val edt = binding.edtSignUpNickname
            val underLine = binding.viewSignUpNickname
            val alertImg = binding.ivSignUpAlert

            // 7자 이상 입력했다면
            if (edt.text.length >= 7) {
                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_1)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                underLine.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_mark_25)

                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE
            }
            // 숫자, 이모지, 특수 문자가 입력됐다면
            else if (!EditTextUtils.nicknameRegex(edt.text.toString())) {
                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_3)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                underLine.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_mark_25)

                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE
            }
            // 6자 이하 입력 및 조건에 맞다면
            else if (edt.text.isNotEmpty()){
                // 닉네임 중복 검사하기
                nicknameOverlap(binding.edtSignUpNickname.text.toString())
            }

            getHideKeyboard(binding.root) // 키보드 숨기기
        }

        /** 닉네임 edt 입력 이벤트 **/
        binding.edtSignUpNickname.onTextChanged {
            binding.tvSignUpOverlapBtn.visibility = View.VISIBLE // 중복 확인 버튼 나타내기
            binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Gray_300)) // view 컬러 기본색으로 변경
            binding.ivSignUpAlert.visibility = View.GONE // 알럿 제거
            binding.tvSignUpAlert.visibility = View.GONE // alert 제거
            binding.cvSignUp1Next.visibility = View.INVISIBLE // 다음 버튼 숨기기
        }

        // 다음 버튼 클릭 이벤트
        binding.btnSignUp1Next.setOnSingleClickListener {
            // 닉네임 변경 불가 팝업 띄우기
            val dialog = NicknameDialog(this@SignUpActivity)
            dialog.nicknameDialogShow()

            // 사용 버튼을 클릭한 경우
            dialog.setOnClickedListener(object : NicknameDialog.ButtonClickListener {
                override fun onClicked() {
                    // 닉네임 저장
                    saveNickname(binding.edtSignUpNickname.text.toString())

                    // 네이버 && 구글 로그인의 경우
                    if (loginId == "naver" || loginId == "google") {
                        // 개인정보 수집 및 이용 동의 바텀 시트 띄우기
                        val termsBottomSheet = TermsBottomSheetFragment { item ->
                            when (item) {
                                0 -> startNextActivity(MoreTermsActivity::class.java) // 개인정보 이용약관 보기 버튼
                                1 -> clickNextBtn() // 다음 버튼
                            }
                        }
                        termsBottomSheet.show(supportFragmentManager, termsBottomSheet.tag)
                    }
                    // Oops 회원가입의 경우
                    else {
                        // 이메일&비밀번호 입력 화면으로 이동
                        startActivityWithClear(SignUp2Activity::class.java)
                    }
                }
            })
        }
    }

    // 네이버 회원가입 또는 구글 회원가입 API 연결
    override fun connectOopsAPI(token: String?, loginId: String?) {
        // 토큰이 있다면, 알림 설정 여부 ok
        var isAlert = false
        if (token != null) {
            isAlert = true
        }

        // 네이버 회원가입 이라면
        if (this.loginId == "naver") {
            // 네이버 회원가입 - 2 API 연결
            val authService = AuthService()
            authService.setSignUpView(this)
            authService.naverSignUp(
                ServerUserModel(
                    serverEmail.toString(),
                    binding.edtSignUpNickname.text.toString(),
                    token,
                    isAlert
                )
            )
        }
        // 구글 회원가입 이라면
        else if (this.loginId == "google") {
            val authService = AuthService()
            authService.setSignUpView(this)
            authService.googleLogin(
                this.loginId!!,
                ServerUserModel(
                    serverEmail.toString(),
                    binding.edtSignUpNickname.text.toString(),
                    token,
                    isAlert
                )
            )
        }
    }

    // 네이버 & 구글 회원가입 시 다음 버튼 클릭 이벤트
    private fun clickNextBtn() {
        try {
            // 다음 버튼 클릭 막기
            binding.btnSignUp1Next.isEnabled = false

            // 안드로이드13 이상이라면
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 알림 수신 권한 설정 창 띄우기
                askNotificationPermission()
            }
            // 안드로이드12 이하라면
            else {
                // FCM 토큰 발급 및 sns 회원가입 API 연결
                getFCMToken(loginId)
            }
        } catch (e: Exception) {
            Log.e("SingUpActivity - clickNextBtn", e.stackTraceToString())
        }
    }

    // 푸시 알림 동의 버튼을 누른 경우
    private fun clickAgreeBtn() {
        val agreeDialog = PushAlertAgreeDialog(this@SignUpActivity)
        agreeDialog.showAgreeDialog()
        agreeDialog.setOnClickedListener(object : PushAlertAgreeDialog.AgreeButtonClickListener {
            override fun onClicked() {
                // 확인 버튼을 누른 경우
                startActivityWithClear(TutorialActivity::class.java)
            }
        })
    }

    // 푸시 알림 동의 안함 버튼을 누른 경우
    private fun clickDisAgreeBtn() {
        val disagreeDialog = PushAlertDisagreeDialog(this@SignUpActivity)
        disagreeDialog.showDisagreeDialog()
        disagreeDialog.setOnClickedListener(object : PushAlertDisagreeDialog.DisAgreeButtonClickListener {
            override fun onClicked() {
                // 확인 버튼을 누른 경우
                startActivityWithClear(TutorialActivity::class.java)
            }
        })
    }

    // 닉네임 중복 검사 체크
    private fun nicknameOverlap(name: String) {
        val authService = AuthService()
        authService.setCommonView(this)
        authService.nicknameOverlap(name)
    }

    // 닉네임 중복 검사 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE

                val alert = binding.tvSignUpAlert
                val alertImg = binding.ivSignUpAlert

                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_confirm)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_confirm_25)

                // 다음 버튼 띄우기
                // 등장 애니메이션 적용
                val inAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_up)
                binding.cvSignUp1Next.visibility = View.VISIBLE
                binding.cvSignUp1Next.startAnimation(inAnim)
            }
        }
    }

    // 닉네임 중복 검사 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            // 닉네임 중복인 경우
            409 -> {
                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE

                val alert = binding.tvSignUpAlert
                val underLine = binding.viewSignUpNickname
                val alertImg = binding.ivSignUpAlert

                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_2)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                underLine.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_mark_25)
            }
            else -> {
                showToast(getString(R.string.toast_server_error))
                startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }

    // 네이버 & 구글 회원가입 성공
    override fun onSignUpSuccess(status: Int, message: String, data: JsonObject?, isGetToken: Boolean) {
        when (status) {
            200 -> {
                try {
                    // spf 기존 값 업데이트
                    saveNickname(binding.edtSignUpNickname.text.toString())
                    saveLoginId(loginId!!)

                    // json 파싱
                    val jsonObject = JSONObject(data.toString())

                    // xAuthToken 저장
                    val xAuthToken: String = jsonObject.getString("xAuthToken").toString()
                    saveToken(xAuthToken)

                    // room db에 최신 유저 정보 저장
                    val userDB = AppDatabase.getUserDB()
                    userDB?.userDao()?.deleteAllUser()
                    userDB?.userDao()?.insertUser(User(loginId!!, binding.edtSignUpNickname.text.toString()))

                    // 기존에 저장되어 있던 모든 알람 삭제(db, 알람 취소)
                    setCancelAllAlarm(this)

                    // 알림 수신 동의했다면(=토큰이 있다면)
                    if (isGetToken) {
                        // 알림 동의 완료 팝업 띄우기
                        clickAgreeBtn()
                    }
                    // 알림 수신 동의를 안 했다면
                    else {
                        // 알림 미동의 완료 팝업 띄우기
                        clickDisAgreeBtn()
                    }
                } catch (e: Exception) {
                    Log.e("SignUpActivity - SignUp", e.stackTraceToString())
                }
            }
        }
    }

    // 네이버 & 구글 회원가입 실패
    override fun onSignUpFailure(status: Int, message: String) {
        showToast(getString(R.string.toast_server_error))
        startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
    }
}