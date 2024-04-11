package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 인벤토리가 없을 경우, 소지품 수정 여부 확인 팝업 */
class InventoryNoStuffModifyDialog(private val context: Context) {
    // dialog 띄우기
    fun showInventoryNoStuffModify() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_inventory_no_stuff_modify)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫치지 않기
        dialog.show()

        // 아니오 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_no_stuff_modify_not_modify).setOnClickListener {
            dialog.dismiss()
        }

        // 예 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_no_stuff_modify_confirm).setOnClickListener {
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