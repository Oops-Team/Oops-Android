package com.example.oops_android.ui.Main.Inventory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemInventoryCategoryRvBinding

/* 인벤토리 화면의 카테고리 버튼 리스트 */
class InventoryCategoryListAdapter(val context: Context): RecyclerView.Adapter<InventoryCategoryListViewHolder>() {

    private var categoryList = ArrayList<CategoryItemUI>() // 리스트
    var onCategoryItemClickListener: ((Int) -> Unit)? = null // 카테고리 클릭 이벤트
    var onCategoryIconClickListener: ((Int, ImageView) -> Unit)? = null // 카테고리 아이콘 클릭 이벤트

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
        holder.binding.fLayoutItemInventoryTop.setOnClickListener {
            onCategoryItemClickListener?.invoke(position)
        }

        // 아이콘 클릭 이벤트
        holder.binding.ivItemInventoryCategoryIcon.setOnClickListener {
            onCategoryIconClickListener?.invoke(position, holder.binding.ivItemInventoryCategoryIcon)
        }
    }

    // 아이템 추가
    fun addCategoryList(categoryList: CategoryList) {
        this.categoryList = categoryList
        notifyItemChanged(categoryList.size)
    }

    // 아이템 활성화 & 비활성화
    @SuppressLint("NotifyDataSetChanged")
    fun setCategorySelected(position: Int) {
        // 기존 아이템 비활성화
        for (i in 0 until categoryList.size) {
            categoryList[i].isSelected = false
        }
        // 새로운 아이템 활성화
        categoryList[position].isSelected = true
        notifyDataSetChanged()
    }

    // 인벤토리 반환
    fun getCategoryItemList(): CategoryList = categoryList as CategoryList

    // 인벤토리 idx 반환
    fun getCategoryIdx(position: Int): Long = categoryList[position].inventoryIdx

    // 인벤토리 아이템 반환
    fun getCategoryItem(position: Int): CategoryItemUI = categoryList[position]

    // 선택된 인벤토리 아이템 반환
    fun getSelectedCategoryItem(): CategoryItemUI? {
        var categoryItem: CategoryItemUI? = null

        for (index in 0 until categoryList.size) {
            if (categoryList[index].isSelected) {
                categoryItem = categoryList[index]
                break
            }
        }

        return categoryItem
    }

    // 인벤토리 아이콘 수정
    fun modifyCategoryItem(position: Int, inventoryIconIdx: Int): Boolean {
        var isChange = false // 아이콘 값 변경에 대한 여부

        // 아이콘이 변경되었다면
        if (categoryList[position].inventoryIconIdx != inventoryIconIdx) {
            categoryList[position].inventoryIconIdx = inventoryIconIdx
            notifyItemChanged(position)
            isChange = true
        }
        return isChange
    }
}