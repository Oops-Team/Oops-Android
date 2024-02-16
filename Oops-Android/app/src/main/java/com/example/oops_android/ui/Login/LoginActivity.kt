package com.example.oops_android.ui.Login

import android.content.res.ColorStateList
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityLoginBinding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.ui.Main.MainActivity
import com.example.oops_android.utils.ButtonUtils
import com.example.oops_android.utils.CustomPasswordTransformationMethod
import com.example.oops_android.utils.onTextChanged

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