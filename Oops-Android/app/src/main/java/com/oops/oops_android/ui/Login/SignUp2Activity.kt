package com.oops.oops_android.ui.Login

import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.db.Entity.User
import com.oops.oops_android.data.remote.Auth.Api.AuthService
import com.oops.oops_android.data.remote.Auth.Api.SignUpView
import com.oops.oops_android.data.remote.Auth.Model.OopsUserModel
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.databinding.ActivitySignUp2Binding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Tutorial.TutorialActivity
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.CustomPasswordTransformationMethod
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.getNickname
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveNickname
import com.oops.oops_android.utils.saveToken
import org.json.JSONObject

/* 회원가입 - 이메일, 비밀번호 입력하는 화면 */
class SignUp2Activity: BaseActivity<ActivitySignUp2Binding>(ActivitySignUp2Binding::inflate), CommonView, SignUpView {

    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수
    private var isPwdCheckMask: Boolean = false // 비밀번호 재확인 mask on/off 변수

    private var isEmailValid: Boolean = false // 이메일 유효성 여부
    private var isPwdValid: Boolean = false // 비밀번호 유효성 여부
    private var isPwdCheckValid: Boolean = false // 비밀번호 재확인 유효성 여부

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // 닉네임 정보 뷰에 보여주기
        binding.tvSignUp2Nickname.text = getNickname()

        // 화면 터치 시 키보드 숨기기
        binding.lLayoutSignUp2Top.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 비밀번호 mask의 스타일 지정
        binding.edtSignUp2Pwd.transformationMethod = CustomPasswordTransformationMethod()
        binding.edtSignUp2PwdCheck.transformationMethod = CustomPasswordTransformationMethod()

        // 이메일 중복 검사 버튼 클릭 이벤트
        binding.tvSignUp2OverlapBtn.setOnClickListener {
            // 이메일 형식이 맞다면
            if (EditTextUtils.emailRegex(binding.edtSignUp2Email.text.toString().trim())) {
                // 중복 확인 검사 API 연결
                val authService = AuthService()
                authService.setCommonView(this)
                authService.emailOverlap(binding.edtSignUp2Email.text.toString())
            }
            else {
                // 이메일 입력 형식 오류 문구 띄우기
                isEmailValid = true
                binding.tvSignUp2EmailAlert.visibility = View.VISIBLE
                binding.tvSignUp2EmailAlert.text = getString(R.string.login_email_alert)
                binding.tvSignUp2EmailAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                binding.viewSignUp2Email.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
            }
        }

        // 비밀번호 - mask on/off 클릭 이벤트
        binding.iBtnSignUp2PwdToggle.setOnClickListener {
            isPwdMask =
                if (isPwdMask) {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnSignUp2PwdToggle, isPwdMask, binding.edtSignUp2Pwd)
                    false
                } else {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnSignUp2PwdToggle, isPwdMask, binding.edtSignUp2Pwd)
                    true
                }
        }

        // 비밀번호 재확인 - mask on/off 클릭 이벤트
        binding.iBtnSignUp2PwdCheckToggle.setOnClickListener {
            isPwdCheckMask =
                if (isPwdCheckMask) {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnSignUp2PwdCheckToggle, isPwdCheckMask, binding.edtSignUp2PwdCheck)
                    false
                } else {
                    ButtonUtils().setOnClickToggleBtn(binding.iBtnSignUp2PwdCheckToggle, isPwdCheckMask, binding.edtSignUp2PwdCheck)
                    true
                }
        }

        // 비밀번호 - 입력 조건 검사
        binding.edtSignUp2Pwd.onTextChanged {
            // 비밀번호 재확인 필드가 작성되어 있다면
            if (binding.edtSignUp2PwdCheck.text.toString().isNotBlank() && isPwdValid) {
                // 두 개의 필드가 다르다면
                if (binding.edtSignUp2Pwd.text.toString() != binding.edtSignUp2PwdCheck.text.toString()) {
                    isPwdCheckValid = false
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.ivSignUp2PwdAlert.visibility = View.INVISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_1)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                    binding.viewSignUp2PwdCheck.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                }
                // 두 개의 필드가 같다면
                else {
                    isPwdCheckValid = true
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.ivSignUp2PwdAlert.visibility = View.VISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_confirm)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    binding.viewSignUp2PwdCheck.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid()
                }
            }

            // 15자 이상 이라면
            if (binding.edtSignUp2Pwd.length() >= 15) {
                isPwdValid = false
                // 알럿 텍스트 출력
                binding.tvSignUp2PwdAlert.text = getString(R.string.signup_pwd_alert_2)
                binding.viewSignUp2Pwd.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2PwdAlert.visibility = View.VISIBLE
            }
            // 8자 이하라면
            else if (binding.edtSignUp2Pwd.length() < 8) {
                isPwdValid = false
                // 알럿 텍스트 출력
                binding.tvSignUp2PwdAlert.text = getString(R.string.signup_pwd_alert_4)
                binding.viewSignUp2Pwd.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2PwdAlert.visibility = View.VISIBLE
            }
            // 영문, 숫자, 특수 문자가 없다면
            else if (!EditTextUtils.passwordRegex(binding.edtSignUp2Pwd.text.toString())) {
                isPwdValid = false
                // 알럿 텍스트 출력
                binding.tvSignUp2PwdAlert.text = getString(R.string.signup_pwd_alert_3)
                binding.viewSignUp2Pwd.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2PwdAlert.visibility = View.VISIBLE
            }
            else {
                // 조건에 부합하다면
                if (EditTextUtils.passwordRegex(binding.edtSignUp2Pwd.text.toString())) {
                    isPwdValid = true
                    binding.tvSignUp2PwdAlert.visibility = View.INVISIBLE
                    binding.viewSignUp2Pwd.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid() // 모든 유효성 확인
                }
            }
        }

        // 비밀번호 재확인 - 입력 조건 검사
        binding.edtSignUp2PwdCheck.onTextChanged {
            // 입력이 되어있는 상태라면
            if (binding.edtSignUp2PwdCheck.text.toString().isNotBlank() && isPwdValid) {
                // 비밀번호와 비밀번호 재확인 필드가 일치하지 않는다면
                if (binding.edtSignUp2Pwd.text.toString() != binding.edtSignUp2PwdCheck.text.toString()) {
                    isPwdCheckValid = false
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.ivSignUp2PwdAlert.visibility = View.INVISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_1)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                    binding.viewSignUp2PwdCheck.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                }
                // 일치한다면
                else {
                    isPwdCheckValid = true
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_confirm)
                    binding.ivSignUp2PwdAlert.visibility = View.VISIBLE
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    binding.viewSignUp2PwdCheck.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid() // 모든 유효성 확인
                }
            }
            // 입력이 안 되어있는 상태라면
            else {
                binding.tvSignUp2PwdCheckAlert.visibility = View.INVISIBLE
                binding.ivSignUp2PwdAlert.visibility = View.INVISIBLE
                binding.viewSignUp2PwdCheck.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Gray_300))
            }
        }
    }

    // 이메일, 비밀번호, 비밀번호 재확인 유효성 검사
    private fun checkValid() {
        if (isEmailValid && isPwdValid && isPwdCheckValid) {
            // 개인정보 수집 및 이용 동의 바텀 시트 띄우기
            val termsBottomSheet = TermsBottomSheetFragment { item ->
                when (item) {
                    0 -> clickVitalText() // 개인정보 이용약관 보기 버튼
                    1 -> clickNextBtn() // 다음 버튼
                }
            }
            termsBottomSheet.show(supportFragmentManager, termsBottomSheet.tag)
        }
    }

    // 개인정보 이용약관 보기 버튼 클릭 이벤트
    private fun clickVitalText() {
        startNextActivity(MoreTermsActivity::class.java)
    }

    // 동의 버튼을 누른 경우
    private fun clickAgreeBtn() {
        val agreeDialog = PushAlertAgreeDialog(this@SignUp2Activity)
        agreeDialog.showAgreeDialog()
        agreeDialog.setOnClickedListener(object : PushAlertAgreeDialog.AgreeButtonClickListener {
            // 확인 버튼을 누른 경우
            override fun onClicked() {
                // 튜토리얼 화면으로 이동
                startActivityWithClear(TutorialActivity::class.java)
            }
        })
    }

    // 동의 안함 버튼을 누른 경우
    private fun clickDisAgreeBtn() {
        val disagreeDialog = PushAlertDisagreeDialog(this@SignUp2Activity)
        disagreeDialog.showDisagreeDialog()
        disagreeDialog.setOnClickedListener(object : PushAlertDisagreeDialog.DisAgreeButtonClickListener {
            override fun onClicked() {
                // 확인 버튼을 누른 경우
                startActivityWithClear(TutorialActivity::class.java)
            }
        })
    }

    // Oops 회원가입 API 연동
    override fun connectOopsAPI(token: String?, loginId: String?) {
        val authService = AuthService()
        authService.setSignUpView(this@SignUp2Activity)
        Log.d("SignUp2Activity", "FCM 토큰 불러오기: " + token.toString())
        authService.oopsSignUp(
            OopsUserModel(
                binding.edtSignUp2Email.text.toString(),
                binding.edtSignUp2Pwd.text.toString(),
                getNickname(),
                token
            )
        )
    }

    // 다음 버튼 클릭 이벤트
    private fun clickNextBtn() {
        // 안드로이드13 이상이라면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 알림 수신 권한 설정 창 띄우기
            askNotificationPermission()
        }
        // 안드로이드12 이하라면
        else {
            // FCM 토큰 발급 및 회원가입 API 연결
            getFCMToken("oops")
        }
    }

    // 이메일 중복 확인 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            // 중복이 아니라면
            200 -> {
                isEmailValid = true
                binding.edtSignUp2Email.isEnabled = false // 이메일 입력 막기
                binding.ivSignUp2EmailAlert.visibility = View.VISIBLE
                binding.tvSignUp2EmailAlert.visibility = View.VISIBLE
                binding.tvSignUp2EmailAlert.text = getString(R.string.signup_email_alert_confirm)
                binding.tvSignUp2EmailAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.viewSignUp2Email.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.tvSignUp2OverlapBtn.visibility = View.INVISIBLE
                checkValid() // 모든 유효성 확인
            }
        }
    }

    // 이메일 중복 확인 실패
    override fun onCommonFailure(status: Int, message: String) {
        when (status) {
            // 이메일 중복
            409 -> {
                isEmailValid = true
                binding.tvSignUp2EmailAlert.visibility = View.VISIBLE
                binding.tvSignUp2EmailAlert.text = getString(R.string.signup_email_alert)
                binding.tvSignUp2EmailAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                binding.viewSignUp2Email.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
            }
            else -> showToast(getString(R.string.toast_server_error))
        }
    }

    // Oops 회원가입 성공
    override fun onSignUpSuccess(status: Int, message: String, data: Any?, isGetToken: Boolean) {
        when (status) {
            200 -> {
                val userDB = AppDatabase.getUserDB()!! // room db의 user db

                // 기존 Room DB에 저장된 값 삭제
                userDB.userDao().deleteAllUser()

                // json 파싱
                val jsonObject = JSONObject(data.toString())

                // accessToken 저장
                val accessToken: String = jsonObject.getString("accessToken").toString()
                saveToken(accessToken)

                // spf 업데이트
                saveNickname(getNickname())

                // Room DB에 값 저장
                userDB.userDao().insertUser(User("oops", getNickname()))

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
            }
        }
    }

    // Oops 회원가입 실패
    override fun onSignUpFailure(status: Int, message: String) {
        when (status) {
            -1 -> showToast(getString(R.string.toast_server_error))
            else -> showToast(getString(R.string.toast_server_error))
        }
    }
}