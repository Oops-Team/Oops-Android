package com.example.oops_android.ui.Main.Inventory

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.R
import com.example.oops_android.databinding.ItemInventoryCategoryRvBinding

/* 인벤토리 화면의 카테고리 RV 뷰 홀더 */
class InventoryCategoryListViewHolder(val context: Context, val binding: ItemInventoryCategoryRvBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CategoryItemUI) {
        val categoryBtn = binding.btnInventoryCategory

        // 아이콘 값 적용
        when (item.inventoryIconIdx) {
            1L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_time_inventory_20, 0, 0, 0)
            2L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_run_inventory_20, 0, 0, 0)
            3L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wallet_inventory_25, 0, 0, 0)
            4L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_computer_inventory_25, 0, 0, 0)
            5L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wheel_inventory_21, 0, 0, 0)
            -1L -> categoryBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_all_box_inventory_24, 0, 0, 0)
        }

        // 이름 적용
        categoryBtn.text = item.inventoryName

        // 인벤토리가 all이라면
        if (item.inventoryIdx == -1L) {
            categoryBtn.setTextAppearance(R.style.H2)
        }

        // 인벤토리가 선택되어 있다면
        if (item.isSelected) {
            categoryBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#40FFFFFF"))
            categoryBtn.setTextColor(ContextCompat.getColor(context, R.color.White))
        }
        else {
            categoryBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_500))
            categoryBtn.setTextColor(Color.parseColor("#B3FFFFFF"))
        }
    }
}
