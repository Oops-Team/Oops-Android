package com.example.oops_android.utils

import android.text.method.HideReturnsTransformationMethod
import android.widget.EditText
import android.widget.ImageButton
import com.example.oops_android.R

// 버튼 이벤트와 관련한 함수 및 변수를 저장한 파일
class ButtonUtils {
    // mask 버튼 on/off 클릭 이벤트 함수
    fun setOnClickToggleBtn(toggleIBtn: ImageButton, isPwdMask: Boolean, pwdEdt: EditText) {
        // 비밀번호가 보인다면
        if (isPwdMask) {
            toggleIBtn.setImageResource(R.drawable.ic_pwd_mask_on_24)
            /* 기본으로 제공하는 메소드
            binding.edtLoginPwd.transformationMethod = PasswordTransformationMethod.getInstance()
            */
            pwdEdt.transformationMethod = CustomPasswordTransformationMethod() // 커스텀 메소드 적용 '●'
        }
        // 비밀번호가 안 보인다면
        else {
            toggleIBtn.setImageResource(R.drawable.ic_pwd_mask_off_24)
            pwdEdt.transformationMethod = HideReturnsTransformationMethod.getInstance() // 비밀번호 보여주기
        }
        pwdEdt.setSelection(pwdEdt.length()) // 커서를 끝으로 이동
    }
}