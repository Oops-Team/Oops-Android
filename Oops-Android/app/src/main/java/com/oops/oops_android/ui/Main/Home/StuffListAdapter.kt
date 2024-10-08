package com.oops.oops_android.ui.Main.Home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
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

    // 소지품 상태 내보내기
    fun getStuffState(position: Int): Boolean {
        return stuffList[position].isTakeStuff!!
    }

    // 소지품 선택 상태 변경
    @SuppressLint("NotifyDataSetChanged")
    fun changeStuffState(position: Int, isTakeStuff: Boolean) {
        stuffList[position].isTakeStuff = isTakeStuff
        notifyDataSetChanged()
    }

    // 소지품 클릭 시간 업데이트
    @SuppressLint("NotifyDataSetChanged")
    fun updateStuffClickTime(position: Int, lastClickTime: Long) {
        stuffList[position].lastClickTime = lastClickTime
        notifyDataSetChanged()
    }

    // 소지품 이름 정보 가져오기
    fun getStuffName(position: Int): String {
        return stuffList[position].stuffName
    }

    // 소지품 내보내기
    fun getStuff(position: Int): StuffItem = stuffList[position]

    // 이름에 따른 소지품 내보내기
    fun getStuffToName(stuffName: String): StuffItem? {
        return stuffList.find { it.stuffName == stuffName }
    }

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