package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import com.oops.oops_android.R

/* 소지품 추가 완료 및 기존 인벤토리 수정 안 함 팝업 */
class InventoryStuffModifyDisagreeDialog(val context: Context) {
    // dialog 띄우기
    fun showInventoryStuffModifyDisagreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_inventory_stuff_modify_disagree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫치지 않기
        dialog.show()

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_home_inventory_disagree_confirm).setOnClickListener {
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