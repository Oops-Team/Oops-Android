package com.example.oops_android.ui.Main.Inventory

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.example.oops_android.R

/* 인벤토리 삭제 팝업 */
class InventoryDeleteDialog(private val context: Context) {
    fun showInventoryDeleteDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_inventory_delete)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 취소 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_delete_cancel).setOnClickListener {
            dialog.dismiss()
        }

        // 삭제 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_delete_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface InventoryDeleteBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: InventoryDeleteBtnClickListener

    fun setOnClickedListener(listener: InventoryDeleteBtnClickListener) {
        onClickListener = listener
    }
}