package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R
import com.oops.oops_android.utils.setOnSingleClickListener

/* 기존 인벤토리 수정 팝업 */
class InventoryStuffModifyDialog(private val context: Context) {
    // dialog 띄우기
    fun showInventoryStuffModifyDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_inventory_stuff_modify)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫치지 않기
        dialog.show()

        // 수정 안함 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_stuff_modify_not_modify).setOnClickListener {
            onClickListener.onClicked(false)
            dialog.dismiss()
        }

        // 수정 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_inventory_stuff_modify_confirm).setOnSingleClickListener {
            onClickListener.onClicked(true)
            dialog.dismiss()
        }
    }

    interface ButtonClickListener {
        fun onClicked(isModify: Boolean)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }
}