package com.example.oops_android.ui.Main.Inventory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemInventoryCategoryRvBinding

/* 인벤토리 화면의 카테고리 버튼 리스트 */
class InventoryCategoryListAdapter(val context: Context): RecyclerView.Adapter<InventoryCategoryListViewHolder>() {

    private var categoryList = ArrayList<CategoryItemUI>() // 리스트
    var onCategoryItemClickListener: ((Int) -> Unit)? = null // 카테고리 클릭 이벤트

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryCategoryListViewHolder {
        val binding: ItemInventoryCategoryRvBinding = ItemInventoryCategoryRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InventoryCategoryListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: InventoryCategoryListViewHolder, position: Int) {
        holder.bind(categoryList[position])

        // 카테고리 클릭 이벤트
        holder.binding.btnInventoryCategory.setOnClickListener {
            onCategoryItemClickListener?.invoke(position)
        }
    }

    // 아이템 추가
    fun addCategoryList(item: CategoryItemUI) {
        categoryList.add(item)
        notifyItemChanged(categoryList.size)
    }

    // 아이템 활성화 & 비활성화
    @SuppressLint("NotifyDataSetChanged")
    fun setCategorySelected(position: Int): Long {
        // 기존 아이템 비활성화
        for (i in 0 until categoryList.size) {
            categoryList[i].isSelected = false
        }
        // 새로운 아이템 활성화
        categoryList[position].isSelected = true
        notifyDataSetChanged()

        return categoryList[position].inventoryIdx // 인벤토리 idx 반환
    }
}