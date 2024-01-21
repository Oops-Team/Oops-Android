package com.example.oops_android.ui.Login

import com.example.oops_android.databinding.ActivityLoginBinding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.utils.ButtonUtils
import com.example.oops_android.utils.CustomPasswordTransformationMethod

class LoginActivity: BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private var isPwdMask: Boolean = false // 비밀번호 mask on/off 변수

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {

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
    }
}