package com.oops.oops_android.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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

// 버튼 클릭 이벤트 처리 클래스(중복 클릭 이슈 방지)
class OnSingleClickListener(private val onSingleClick: (View) -> Unit) : View.OnClickListener {
    companion object {
        private const val CLICK_INTERVAL = 500L
    }

    private var clickable = true

    override fun onClick(v: View?) {
        if (clickable) {
            clickable = false
            v?.run {
                postDelayed({
                    clickable = true
                }, CLICK_INTERVAL)
                onSingleClick(v)
            }
        } else {
            Log.i("OnThrottleClickListener_onClick", "기다리는 중")
        }
    }
}

// 버튼 클릭 이벤트 처리 확장 함수(중복 클릭 이슈 방지)
fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    val singleClickListener = OnSingleClickListener { onSingleClick(it) }
    setOnClickListener(singleClickListener)
}