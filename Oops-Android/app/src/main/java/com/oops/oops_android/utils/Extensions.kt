package com.oops.oops_android.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

// Edt 텍스트 입력 이벤트 확장 함수
fun EditText.onTextChanged(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {

        // 텍스트 변경 전
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        // 텍스트 변경 중
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        // 텍스트 변경 후
        override fun afterTextChanged(s: Editable?) {
            completion(s)
        }
    } )
}