package com.oops.oops_android.ui.Main.Inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemInventoryStuffBoxRvBinding
import com.oops.oops_android.ui.Main.Home.StuffItem

/* 인벤토리 화면의 소지품 리스트 */
class InventoryStuffListAdapter(private val stuffList: ArrayList<StuffItem>): RecyclerView.Adapter<InventoryStuffListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryStuffListViewHolder {
        val binding: ItemInventoryStuffBoxRvBinding = ItemInventoryStuffBoxRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return InventoryStuffListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryStuffListViewHolder, position: Int) {
        holder.bind(stuffList[position])
    }

    override fun getItemCount(): Int = stuffList.size
}