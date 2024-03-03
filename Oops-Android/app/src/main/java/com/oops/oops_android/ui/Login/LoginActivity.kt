package com.oops.oops_android.ui.Login

import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ActivityLoginBinding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Main.MainActivity
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.CustomPasswordTransformationMethod
import com.oops.oops_android.utils.getLoginId
import com.oops.oops_android.utils.getNickname
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveEmail
import com.oops.oops_android.utils.saveLoginId

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private var isAllInput: Boolean = false // 모든 edt가 입력되었는지에 대한 여부
    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수

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

        // 비밀번호 mask의 메소드(스타일) 지정
        binding.edtLoginPwd.transformationMethod = CustomPasswordTransformationMethod()

        // 회원가입 페이지로 이동
        binding.tvLoginSignup.setOnClickListener {
            startActivityWithClear(SignUpActivity::class.java)
        }

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

        binding.edtLoginEmail.onTextChanged {
            checkEdtInput()
        }
        binding.edtLoginPwd.onTextChanged {
            checkEdtInput()
        }

        // 로그인 버튼 클릭 이벤트
        binding.btnLoginConfirm.setOnClickListener {
            if (isAllInput) {
                // 홈 화면으로 이동
                startActivityWithClear(MainActivity::class.java)
            }
        }

        // 최근 로그인한 플랫폼에 따른 말풍선 띄우기
        if (getLoginId() == "naver") {
            binding.lLayoutLoginRecentNaver.visibility = View.VISIBLE
        }
        else if (getLoginId() == "google") {
            binding.lLayoutLoginRecentGoogle.visibility = View.VISIBLE
        }

        // 네이버 로그인 버튼 클릭 이벤트
        binding.iBtnLoginNaver.setOnClickListener {
            // 재로그인이라면
            if (getNickname().isNotEmpty()) {
                // 홈 화면으로 이동
                startActivityWithClear(MainActivity::class.java)
            }
            // 신규 가입이라면
            else {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    // 인증 성공
                    override fun onSuccess() {
                        // 네이버api에서 프로필 정보 가져오기
                        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                            // 호출 성공
                            override fun onSuccess(result: NidProfileResponse) {
                                // 사용자의 이메일 저장
                                saveEmail(result.profile?.email.toString())

                                // 최근에 로그인한 플랫폼 저장
                                saveLoginId("naver")

                                // 닉네임 설정 화면으로 이동
                                startActivityWithClear(SignUpActivity::class.java)
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
                        Log.e("LoginActivity", "errorCode : $errorCode errorDescription: $errorDescription")
                        showCustomSnackBar(R.string.toast_login_naver_failure)
                    }

                    // 인증 오류
                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }
                }

                NaverIdLoginSDK.authenticate(this, oAuthLoginCallback)
            }
        }
    }

    // 입력 여부 체크
    private fun checkEdtInput() {
        // id와 pwd가 다 입력되었다면
        // TODO: 입력 내용에 따른 로직 처리(with api 연동)
        isAllInput =
            if (binding.edtLoginEmail.text.isNotEmpty() && binding.edtLoginPwd.text.isNotEmpty()) {
                binding.btnLoginConfirm.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Main_500))
                binding.btnLoginConfirm.setTextColor(getColor(R.color.White))
                true
            }
            else {
                binding.btnLoginConfirm.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Gray_100))
                binding.btnLoginConfirm.setTextColor(getColor(R.color.Gray_400))
                false
        }
    }
}