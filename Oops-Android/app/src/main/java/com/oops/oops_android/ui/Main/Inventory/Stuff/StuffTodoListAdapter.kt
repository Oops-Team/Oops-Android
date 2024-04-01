package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemTutorialStuffBinding
import com.oops.oops_android.ui.Main.Home.StuffItem

// 챙겨야 할 것 화면의 소지품 목록 어댑터
class StuffTodoListAdapter(private val stuffList: ArrayList<StuffItem>) : RecyclerView.Adapter<StuffTodoListViewHolder>() {

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null // 소지품 롱 클릭

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffTodoListViewHolder {
        val binding: ItemTutorialStuffBinding = ItemTutorialStuffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StuffTodoListViewHolder(binding)
    }

    override fun getItemCount(): Int = stuffList.size

    override fun onBindViewHolder(holder: StuffTodoListViewHolder, position: Int) {
        holder.bind(stuffList[position])

        // 소지품 롱 클릭
        holder.binding.lLayoutItemTutorialTop.setOnLongClickListener { view ->
            onItemLongClickListener?.onItemLongClick(view, position)
            true
        }
    }
}