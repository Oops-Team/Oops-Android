package com.oops.oops_android.ui.Login

import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
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
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveLoginId
import com.oops.oops_android.utils.saveNickname
import com.oops.oops_android.utils.saveToken
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
        binding.tvSignUpOverlapBtn.setOnClickListener {
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
            // 다음 버튼 숨기기
            binding.cvSignUp1Next.visibility = View.INVISIBLE

            // 10자 미만 입력됐다면
            if (binding.edtSignUpNickname.length() in 1..6) {
                binding.tvSignUpOverlapBtn.visibility = View.VISIBLE // 중복 확인 버튼 나타내기
                binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Gray_300))
                binding.ivSignUpAlert.visibility = View.GONE // 알럿 제거
                binding.tvSignUpAlert.visibility = View.GONE // alert 제거
            }
        }

        // 다음 버튼 클릭 이벤트
        binding.btnSignUp1Next.setOnClickListener {
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

    override fun connectOopsAPI(token: String?, tempLoginId: String?) {
        val authService = AuthService()
        authService.setSignUpView(this)

        var isAlert = false
        if (token != null) {
            isAlert = true // 알림 설정 여부
        }
        authService.serverLogin(
            loginId!!,
            ServerUserModel(
                serverEmail.toString(),
                binding.edtSignUpNickname.text.toString(),
                token,
                isAlert
            )
        )
    }

    // 네이버 & 구글 로그인 시 다음 버튼 클릭 이벤트
    private fun clickNextBtn() {
        try {
            // 네이버에서 넘어온 화면이라면
            if (loginId == "naver" || loginId == "google") {

                // 안드로이드13 이상이라면
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 알림 수신 권한 설정 창 띄우기
                    askNotificationPermission()
                }
                // 안드로이드12 이하라면
                else {
                    // FCM 토큰 발급 및 로그인 API 연결
                    getFCMToken(loginId)
                }
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
            }
        }
    }

    // 네이버 & 구글 회원가입 성공
    override fun onSignUpSuccess(status: Int, message: String, data: Any?, isGetToken: Boolean) {
        when (status) {
            200 -> {
                // spf 기존 값 업데이트
                saveNickname(binding.edtSignUpNickname.text.toString())
                saveLoginId(loginId!!)

                // json 파싱
                val jsonObject = JSONObject(data.toString())

                // xAuthToken 저장
                val xAuthToken: String = jsonObject.getString("xAuthToken").toString()
                saveToken(xAuthToken)

                if (loginId == "naver") {
                    // room db의 user db
                    val userDB = AppDatabase.getUserDB()
                    userDB?.userDao()?.deleteAllUser()
                    userDB?.userDao()?.insertUser(User(loginId!!, binding.edtSignUpNickname.text.toString()))
                }

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

    // 네이버 & 구글 로그인 실패
    override fun onSignUpFailure(status: Int, message: String) {
        showToast(getString(R.string.toast_server_error))
    }
}