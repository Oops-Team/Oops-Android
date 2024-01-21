package com.example.oops_android.utils

import android.text.method.PasswordTransformationMethod
import android.view.View
import java.util.regex.Pattern

// EditText와 관련한 함수 및 변수를 저장한 파일
class EditTextUtils {
    companion object {
        // 이메일 정규식
        private const val emailPattern: String = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        // 비밀번호 패턴(영어, 숫자, 일부 특수문자 허용)
        private const val passwordPattern: String ="^(?=.*[a-z])(?=.*[0-9])(?=.*[?!*~@#%^])[a-zA-Z0-9?!*~@#%^]{8,15}$"

        // 이메일 정규식 확인
        fun emailRegex(email: String): Boolean {
            return email.matches(emailPattern.toRegex())
        }

        // 비밀번호 패턴 일치 확인
        fun passwordRegex(password: String): Boolean {
            return password.matches(passwordPattern.toRegex())
        }
    }
}

// 기본 hide 텍스트를 '●'로 커스텀하는 클래스
class CustomCharSequence(private val source: CharSequence): CharSequence {
    override val length: Int
        get() = source.length

    override fun get(index: Int): Char = '●'

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        source.subSequence(startIndex, endIndex)
}

class CustomPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence =
        CustomCharSequence(source)
}