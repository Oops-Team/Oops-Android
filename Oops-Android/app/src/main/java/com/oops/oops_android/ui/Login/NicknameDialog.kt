package com.oops.oops_android.ui.Login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

// 닉네임 변경 불가 팝업
class NicknameDialog(private val context: Context) {
    // dialog 띄우기
    fun nicknameDialogShow() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_nickname)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫치지 않기
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_nickname_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 사용 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.popup_nickname_use).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface ButtonClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }
}