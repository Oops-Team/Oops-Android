package com.example.oops_android.ui.Login

import com.example.oops_android.databinding.ActivitySignUp2Binding
import com.example.oops_android.ui.BaseActivity
import com.example.oops_android.utils.ButtonUtils
import com.example.oops_android.utils.CustomPasswordTransformationMethod
import com.example.oops_android.utils.EditTextUtils
import com.example.oops_android.utils.onTextChanged
import java.util.regex.Pattern

// 회원가입 - 이메일, 비밀번호 입력하는 화면
class SignUp2Activity: BaseActivity<ActivitySignUp2Binding>(ActivitySignUp2Binding::inflate) {

    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수
    private var isPwdCheckMask: Boolean = false // 비밀번호 재확인 mask on/off 변수

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {

        // 비밀번호 mask의 스타일 지정
        binding.edtSignUp2Pwd.transformationMethod = CustomPasswordTransformationMethod()
        binding.edtSignUp2PwdCheck.transformationMethod = CustomPasswordTransformationMethod()

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
    }
}