package com.oops.oops_android.ui.Main.Home.Todo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 일정 추가 성공 팝업 */
class TodoCreateDialog(private val context: Context) {
    fun showTodoCreateDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_todo_create)
        dialog.setCancelable(false)
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_todo_create_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface TodoCreateBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: TodoCreateBtnClickListener

    fun setOnClickedListener(listener: TodoCreateBtnClickListener) {
        onClickListener = listener
    }
}