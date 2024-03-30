package com.oops.oops_android.ui.Main.Sting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemFriendsBoxRv3Binding

// 사용자 검색 결과 목록 어댑터
class SearchFriendsListAdapter(private val userList: ArrayList<FriendsItem>): RecyclerView.Adapter<SearchFriendsListViewHolder>() {
    var onItemClickListener1: ((Int) -> Unit)? = null // 친구 신청 버튼 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendsListViewHolder {
        val binding: ItemFriendsBoxRv3Binding = ItemFriendsBoxRv3Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchFriendsListViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: SearchFriendsListViewHolder, position: Int) {
        holder.bind(userList[position])

        // 친구 신청 버튼 클릭 이벤트
        holder.binding.tvFriendsBox3Add.setOnClickListener {
            onItemClickListener1?.invoke(position)
        }
    }
}