package com.example.oops_android.ui.Main.Inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemInventoryStuffBoxRvBinding
import com.example.oops_android.ui.Main.Home.StuffItem

/* 인벤토리 화면의 소지품 리스트 */
class InventoryStuffListAdapter(val context: Context): RecyclerView.Adapter<InventoryStuffListViewHolder>() {

    private var stuffList = ArrayList<StuffItem>() // 리스트
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryStuffListViewHolder {
        val binding: ItemInventoryStuffBoxRvBinding = ItemInventoryStuffBoxRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InventoryStuffListViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: InventoryStuffListViewHolder, position: Int) {
        holder.bind(stuffList[position])
    }

    override fun getItemCount(): Int = stuffList.size

    // 소지품 추가
    fun addStuffList(item: StuffItem) {
        stuffList.add(item)
        notifyItemChanged(stuffList.size)
    }
}