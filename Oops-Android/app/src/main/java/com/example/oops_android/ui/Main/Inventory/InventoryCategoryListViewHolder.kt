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
        val iconImg = binding.ivItemInventoryCategoryIcon // 아이콘
        val categoryName = binding.tvItemInventoryCategoryName // 인벤토리 이름
        val categoryLayout = binding.fLayoutItemInventoryTop // 인벤토리 배경

        // 아이콘 값 적용
        when (item.inventoryIconIdx) {
            1 -> iconImg.setImageResource(R.drawable.ic_time_inventory_20)
            2 -> iconImg.setImageResource(R.drawable.ic_run_inventory_20)
            3 -> iconImg.setImageResource(R.drawable.ic_wallet_inventory_25)
            4 -> iconImg.setImageResource(R.drawable.ic_computer_inventory_25)
            5 -> iconImg.setImageResource(R.drawable.ic_wheel_inventory_21)
            -1 -> iconImg.setImageResource(R.drawable.ic_all_box_inventory_24)
        }

        // 이름 적용
        categoryName.text = item.inventoryName

        // 인벤토리가 all이라면
        if (item.inventoryIdx == -1L) {
            categoryName.setTextAppearance(R.style.H2)
        }

        // 인벤토리가 선택되어 있다면
        if (item.isSelected) {
            categoryLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#40FFFFFF"))
            categoryName.setTextColor(ContextCompat.getColor(context, R.color.White))
        }
        else {
            categoryLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_500))
            categoryName.setTextColor(Color.parseColor("#B3FFFFFF"))
        }
    }
}
