package com.oops.oops_android.ui.Main.MyPage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 로그아웃 확인 팝업 */
class LogoutDialog(private val context: Context) {
    fun showLogoutDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_logout)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_logout_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 로그아웃 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_logout_yes).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface LogoutBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: LogoutBtnClickListener

    fun setOnClickedListener(listener: LogoutBtnClickListener) {
        onClickListener = listener
    }
}