package com.oops.oops_android.ui.Main.Home.Todo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 일정 삭제 완료 팝업 */
class TodoDeleteAgreeDialog(val context: Context) {
    fun showTodoDeleteAgreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_todo_delete_agree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_todo_delete_agree_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface TodoDeleteAgreeBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: TodoDeleteAgreeBtnClickListener

    fun setOnClickedListener(listener: TodoDeleteAgreeBtnClickListener) {
        onClickListener = listener
    }
}