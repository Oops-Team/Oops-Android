package com.oops.oops_android.ui.Main.Home.Todo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 일정 수정 성공 팝업 */
class TodoModifyDialog(private val context: Context) {
    fun showTodoModifyDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_todo_modify)
        dialog.setCancelable(false)
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_todo_modify_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface TodoModifyBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: TodoModifyBtnClickListener

    fun setOnClickedListener(listener: TodoModifyBtnClickListener) {
        onClickListener = listener
    }
}