package com.oops.oops_android.ui.Login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 회원가입 시 기존 회원 정보가 있을 경우 뜨는 팝업 */
class SignupEmailOverlapDialog(val context: Context) {
    @SuppressLint("SetTextI18n")
    fun showSignupEmailOverlapDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_signup_email_overlap)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_signup_email_overlap_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface GoLoginBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: GoLoginBtnClickListener

    fun setOnClickedListener(listener: GoLoginBtnClickListener) {
        onClickListener = listener
    }
}