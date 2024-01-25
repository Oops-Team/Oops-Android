package com.example.oops_android.ui.Login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.example.oops_android.R

class PushAlertDialog(private val context: Context) {
    // dialog 띄우기
    fun showPushAlertDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_push_alert)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 동의 안함 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_push_alert_disagree).setOnClickListener {
            onClickListener.onClicked(false)
            dialog.dismiss()
        }

        // 동의 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_push_alert_agree).setOnClickListener {
            onClickListener.onClicked(true)
            dialog.dismiss()
        }
    }

    interface PushAlertButtonClickListener {
        fun onClicked(isAgree: Boolean)
    }

    private lateinit var onClickListener: PushAlertButtonClickListener

    fun setOnClickedListener(listener: PushAlertButtonClickListener) {
        onClickListener = listener
    }
}
