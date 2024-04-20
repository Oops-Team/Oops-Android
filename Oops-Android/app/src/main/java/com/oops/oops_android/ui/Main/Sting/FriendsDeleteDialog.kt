package com.oops.oops_android.ui.Main.Sting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 친구 끊기 여부 재확인 팝업 */
class FriendsDeleteDialog(private val context: Context) {
    fun showFriendsDeleteDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_friends_delete)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_friends_delete_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 예 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_friends_delete_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface FriendsDeleteBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: FriendsDeleteBtnClickListener

    fun setOnClickedListener(listener: FriendsDeleteBtnClickListener) {
        onClickListener = listener
    }
}