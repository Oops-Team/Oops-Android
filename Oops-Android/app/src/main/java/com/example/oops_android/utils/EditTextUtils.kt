package com.example.oops_android.utils

import android.text.method.PasswordTransformationMethod
import android.view.View

// EditText와 관련한 함수 및 변수를 저장한 파일

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