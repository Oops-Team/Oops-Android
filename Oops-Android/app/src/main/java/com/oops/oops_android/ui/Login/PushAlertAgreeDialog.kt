package com.oops.oops_android.ui.Login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.oops.oops_android.R
import com.oops.oops_android.utils.CalendarUtils

// 앱 푸시 알림 동의 팝업(알림 수신에 동의했을 경우 띄워짐)
class PushAlertAgreeDialog(private val context: Context) {
    @SuppressLint("SetTextI18n")
    fun showAgreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_push_alert_agree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_push_alert_agree_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface AgreeButtonClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: AgreeButtonClickListener

    fun setOnClickedListener(listener: AgreeButtonClickListener) {
        onClickListener = listener
    }
}