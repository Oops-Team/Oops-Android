package com.oops.oops_android.ui.Main.Sting

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R

/* 친구 신청 수락, 친구 신청 거절 팝업 */
class FriendsAcceptDialog(val context: Context, val layout: Int, private val btn: Int, val name: String? = null) {
    @SuppressLint("SetTextI18n")
    fun showFriendsAcceptDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(layout)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기

        // name 데이터가 있다면
        if (name != null) {
            // 팝업 내 텍스트 설정
            dialog.findViewById<TextView>(R.id.tv_popup_friends_request).text = name + " " + applicationContext().getString(R.string.popup_friends_request_info)
        }
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(btn).setOnClickListener {
            dialog.dismiss()
        }
    }
}