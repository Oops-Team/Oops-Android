package com.oops.oops_android.ui.Main.Sting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 친구 신청 수락 팝업 */
class FriendsAcceptDialog(val context: Context) {
    fun showFriendsAcceptDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_friends_accept)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_friends_accept_confirm).setOnClickListener {
            dialog.dismiss()
        }
    }
}