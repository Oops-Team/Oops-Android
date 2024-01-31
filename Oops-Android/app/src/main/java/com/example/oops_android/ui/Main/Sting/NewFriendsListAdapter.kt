package com.example.oops_android.ui.Main.Sting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemFriendsBoxRvBinding

// 친구 신청 현황 목록 리스트 어댑터
class NewFriendsListAdapter(val context: Context): RecyclerView.Adapter<NewFriendsListViewHolder>() {
    private var newFriendsList = ArrayList<FriendsItem>() // 친구 신청 현황 목록
    var onFriendsItemClickListener1: ((Int) -> Unit)? = null // 친구 신청 버튼 클릭
    var onFriendsItemClickListener2: ((Int) -> Unit)? = null // 수락&거절 버튼 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewFriendsListViewHolder {
        val binding: ItemFriendsBoxRvBinding = ItemFriendsBoxRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewFriendsListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = newFriendsList.size

    override fun onBindViewHolder(holder: NewFriendsListViewHolder, position: Int) {
        holder.bind(newFriendsList[position])

        // 친구 신청 버튼 클릭 이벤트
        holder.binding.tvFriendsAdd.setOnClickListener {
            onFriendsItemClickListener1?.invoke(position)
        }
        // 수락&거절 클릭 이벤트
        holder.binding.lLayoutFriendsBtn.setOnClickListener {
            onFriendsItemClickListener2?.invoke(position)
        }
    }

    // 친구 신청 목록 추가
    fun addNewFriendsList(friendsItem: FriendsItem) {
        newFriendsList.add(friendsItem)
        notifyItemChanged(newFriendsList.size)
    }
}