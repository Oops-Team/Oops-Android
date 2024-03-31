package com.oops.oops_android.ui.Main.Inventory

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 인벤토리 삭제 완료 팝업 */
class InventoryDeleteAgreeDialog(val context: Context) {
    fun showInventoryDeleteAgreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_inventory_delete_agree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_delete_agree_confirm).setOnClickListener {
            dialog.dismiss()
            onClickListener.onClicked()
        }
    }

    interface InventoryDeleteAgreeBtnClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: InventoryDeleteAgreeBtnClickListener

    fun setOnClickedListener(listener: InventoryDeleteAgreeBtnClickListener) {
        onClickListener = listener
    }
}