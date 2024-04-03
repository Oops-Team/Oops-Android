package com.oops.oops_android.ui.Main.MyPage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 회원 탈퇴 여부 재확인 팝업 */
class WithdrawalDialog(private val context: Context) {
    fun showWithdrawalDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_withdrawal)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_withdrawal_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 회원 탈퇴 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_withdrawal_yes).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface WithdrawalBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: WithdrawalBtnClickListener

    fun setOnClickedListener(listener: WithdrawalBtnClickListener) {
        onClickListener = listener
    }
}