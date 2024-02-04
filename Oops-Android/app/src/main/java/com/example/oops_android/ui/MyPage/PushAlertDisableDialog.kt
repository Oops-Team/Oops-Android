package com.example.oops_android.ui.MyPage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.example.oops_android.R

// 푸시 알림 비활성화 팝업
class PushAlertDisableDialog(private val context: Context) {
    // dialog 띄우기
    fun showPushAlertDisableDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_push_alert_disable)
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_push_alert_disable_cancel).setOnClickListener {
            onClickListener.onClicked(false)
            dialog.dismiss()
        }

        // 비활성화 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_push_alert_disable).setOnClickListener {
            onClickListener.onClicked(true)
            dialog.dismiss()
        }
    }

    interface PushAlertDisableButtonClickListener {
        fun onClicked(isAgree: Boolean)
    }

    private lateinit var onClickListener: PushAlertDisableButtonClickListener

    fun setOnClickedListener(listener: PushAlertDisableButtonClickListener) {
        onClickListener = listener
    }
}