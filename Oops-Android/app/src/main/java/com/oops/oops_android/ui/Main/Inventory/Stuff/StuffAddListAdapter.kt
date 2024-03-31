package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemTutorialStuffBinding

// 소지품 추가 화면의 소지품 목록 어댑터
class StuffAddListAdapter(val stuffList: ArrayList<StuffAddItem>): RecyclerView.Adapter<StuffAddListViewHolder>() {
    var onItemClickListener: ((Int) -> Unit)? = null // 소지품 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffAddListViewHolder {
        val binding: ItemTutorialStuffBinding = ItemTutorialStuffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StuffAddListViewHolder(binding)
    }

    override fun getItemCount(): Int = stuffList.size

    override fun onBindViewHolder(holder: StuffAddListViewHolder, position: Int) {
        holder.bind(stuffList[position])

        // 소지품 클릭
        holder.binding.lLayoutItemTutorialTop.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }
}