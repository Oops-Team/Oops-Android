package com.oops.oops_android.ui.Main.Home.Todo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R
import com.oops.oops_android.utils.setOnSingleClickListener

/* 일정 삭제 확인 팝업 */
class TodoDeleteDialog(private val context: Context) {
    fun showTodoDeleteDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_todo_delete)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 아니오 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_todo_delete_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 예 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_todo_delete_yes).setOnSingleClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface TodoDeleteBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: TodoDeleteBtnClickListener

    fun setOnClickedListener(listener: TodoDeleteBtnClickListener) {
        onClickListener = listener
    }
}