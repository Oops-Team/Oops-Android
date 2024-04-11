package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 인벤토리가 없을 경우, 소지품 수정 완료 팝업 */
class InventoryNoStuffModifyAgreeDialog(private val context: Context) {
    // dialog 띄우기
    fun showInventoryNoStuffModifyAgreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_inventory_no_stuff_modify_agree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫치지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_no_stuff_modify_agree_confirm).setOnClickListener {
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