package com.oops.oops_android.ui.Main.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemHomeStuffBinding

// 홈 화면의 챙겨야 할 것 목록 어댑터
class StuffListAdapter(val context: Context): RecyclerView.Adapter<StuffListViewHolder>() {

    private var stuffList = ArrayList<StuffItem>() // 소지품 목록
    var onItemClickListener: ((Int) -> Unit)? = null // 소지품 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffListViewHolder {
        val binding: ItemHomeStuffBinding = ItemHomeStuffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StuffListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = stuffList.size

    override fun onBindViewHolder(holder: StuffListViewHolder, position: Int) {
        holder.bind(stuffList[position])

        // 소지품 클릭
        holder.binding.cLayoutHomeStuffTop.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    // 소지품 초기화
    fun resetStuffList() {
        notifyItemRangeRemoved(0, stuffList.size)
        stuffList.clear()
    }

    // 소지품 추가
    fun addStuffList(stuffItem: StuffItem) {
        stuffList.add(stuffItem)
        notifyItemChanged(stuffList.size)
    }

    // 소지품 이름 정보 가져오기
    fun getStuffName(position: Int): String {
        return stuffList[position].stuffName
    }

    // 소지품 내보내기
    fun getStuff(position: Int): StuffItem = stuffList[position]

    // 전체 소지품 목록 내보내기
    fun getStuffList(): ArrayList<StuffItem> = stuffList

    // 소지품 삭제
    fun deleteStuff(item: StuffItem) {
        val index = stuffList.indexOf(item)
        stuffList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, stuffList.size - index)
    }
}