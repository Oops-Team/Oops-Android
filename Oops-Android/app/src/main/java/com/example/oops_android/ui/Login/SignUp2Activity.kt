package com.example.oops_android.ui.Login

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivitySignUp2Binding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.utils.ButtonUtils
import com.example.oops_android.utils.CustomPasswordTransformationMethod
import com.example.oops_android.utils.EditTextUtils
import com.example.oops_android.utils.onTextChanged

// 회원가입 - 이메일, 비밀번호 입력하는 화면
class SignUp2Activity: BaseActivity<ActivitySignUp2Binding>(ActivitySignUp2Binding::inflate) {

    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수
    private var isPwdCheckMask: Boolean = false // 비밀번호 재확인 mask on/off 변수

    private var isEmailValid: Boolean = false // 이메일 유효성 여부
    private var isPwdValid: Boolean = false // 비밀번호 유효성 여부
    private var isPwdCheckValid: Boolean = false // 비밀번호 재확인 유효성 여부

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {

        // 비밀번호 mask의 스타일 지정
        binding.edtSignUp2Pwd.transformationMethod = CustomPasswordTransformationMethod()
        binding.edtSignUp2PwdCheck.transformationMethod = CustomPasswordTransformationMethod()

        // 이메일 중복 검사 버튼 클릭 이벤트
        binding.tvSignUp2OverlapBtn.setOnClickListener {
            // 이메일 형식이 맞다면
            if (EditTextUtils.emailRegex(binding.edtSignUp2Email.text.toString().trim())) {
                // TODO: 중복 확인 검사하기

                // 중복이 아니라면
                isEmailValid = true
                binding.edtSignUp2Email.isEnabled = false // 이메일 입력 막기
                binding.tvSignUp2EmailAlert.text = getString(R.string.signup_email_alert_confirm)
                binding.tvSignUp2EmailAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.edtSignUp2Email.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.ivSignUp2EmailAlert.visibility = View.VISIBLE
                binding.tvSignUp2OverlapBtn.visibility = View.INVISIBLE
                checkValid() // 모든 유효성 확인

                /*
                // 중복 이라면
                isEmailValid = false
                binding.tvSignUp2EmailAlert.text = R.string.signup_email_alert.toString()
                binding.tvSignUp2EmailAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                binding.edtSignUp2Email.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2OverlapBtn.visibility = View.VISIBLE
                binding.ivSignUp2EmailAlert.visibility = View.INVISIBLE
                */
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
            if (binding.edtSignUp2PwdCheck.text.toString().isNotBlank()) {
                // 두 개의 필드가 다르다면
                if (binding.edtSignUp2Pwd.text.toString() != binding.edtSignUp2PwdCheck.text.toString()) {
                    isPwdCheckValid = false
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_1)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                    binding.edtSignUp2PwdCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                }
                // 두 개의 필드가 같다면
                else {
                    isPwdCheckValid = true
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_confirm)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    binding.edtSignUp2PwdCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid()
                }
            }

            // 15자 이상 이라면
            if (binding.edtSignUp2Pwd.length() >= 15) {
                isPwdValid = false
                // 알럿 텍스트 출력
                binding.tvSignUp2PwdAlert.text = getString(R.string.signup_pwd_alert_2)
                binding.edtSignUp2Pwd.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2PwdAlert.visibility = View.VISIBLE
            }
            // 영문, 숫자, 특수 문자가 없다면
            else if (!EditTextUtils.passwordRegex(binding.edtSignUp2Pwd.text.toString())) {
                isPwdValid = false
                // 알럿 텍스트 출력
                binding.tvSignUp2PwdAlert.text = getString(R.string.signup_pwd_alert_3)
                binding.edtSignUp2Pwd.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                binding.tvSignUp2PwdAlert.visibility = View.VISIBLE
            }
            else {
                // 조건에 부합하다면
                if (EditTextUtils.passwordRegex(binding.edtSignUp2Pwd.text.toString())) {
                    isPwdValid = true
                    binding.tvSignUp2PwdAlert.visibility = View.INVISIBLE
                    binding.ivSignUp2EmailAlert.visibility = View.VISIBLE
                    binding.edtSignUp2Pwd.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid() // 모든 유효성 확인
                }
            }
        }

        // 비밀번호 재확인 - 입력 조건 검사
        binding.edtSignUp2PwdCheck.onTextChanged {
            // 입력이 되어있는 상태라면
            if (binding.edtSignUp2PwdCheck.text.toString().isNotBlank()) {
                // 비밀번호와 비밀번호 재확인 필드가 일치하지 않는다면
                if (binding.edtSignUp2Pwd.text.toString() != binding.edtSignUp2PwdCheck.text.toString()) {
                    isPwdCheckValid = false
                    binding.tvSignUp2PwdCheckAlert.visibility = View.VISIBLE
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_1)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                    binding.edtSignUp2PwdCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                }
                // 일치한다면
                else {
                    isPwdCheckValid = true
                    binding.tvSignUp2PwdCheckAlert.text = getString(R.string.signup_pwd_alert_confirm)
                    binding.tvSignUp2PwdCheckAlert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    binding.edtSignUp2PwdCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Main_500))
                    checkValid() // 모든 유효성 확인
                }
            }
            // 입력이 안 되어있는 상태라면
            else {
                binding.tvSignUp2PwdCheckAlert.visibility = View.INVISIBLE
                binding.edtSignUp2PwdCheck.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Gray_300))
            }
        }
    }

    // 이메일, 비밀번호, 비밀번호 재확인 유효성 검사
    private fun checkValid() {
        // TODO: 개인정보 수집 및 이용 동의 바텀 시트띄우기
        if (isEmailValid && isPwdValid && isPwdCheckValid) {
            showToast("모두 만족!")
        }
        else {
            showToast("1.$isEmailValid, 2.$isPwdValid, 3.$isPwdCheckValid 불만족!")
        }
    }
}