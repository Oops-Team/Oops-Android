package com.example.oops_android.ui.Login

import android.text.method.HideReturnsTransformationMethod
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityLoginBinding
import com.example.oops_android.ui.BaseActivity
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
            // 비밀번호가 보인다면
            if (isPwdMask) {
                binding.iBtnLoginToggle.setImageResource(R.drawable.ic_pwd_mask_on_24)
                /* 기본으로 제공하는 메소드
                 binding.edtLoginPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                 */
                binding.edtLoginPwd.transformationMethod = CustomPasswordTransformationMethod() // 커스텀 메소드 적용 '●'
                binding.edtLoginPwd.setSelection(binding.edtLoginPwd.length()) // 커서를 끝으로 이동
                isPwdMask = false
            }
            // 비밀번호가 안 보인다면
            else {
                binding.iBtnLoginToggle.setImageResource(R.drawable.ic_pwd_mask_off_24)
                binding.edtLoginPwd.transformationMethod = HideReturnsTransformationMethod.getInstance() // 비밀번호 보여주기
                binding.edtLoginPwd.setSelection(binding.edtLoginPwd.length()) // 커서를 끝으로 이동
                isPwdMask = true
            }
        }
    }
}