package com.oops.oops_android.ui.Main.Sting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemFriendsBoxRvBinding

// 친구 신청 현황 목록 리스트 어댑터
class NewFriendsListAdapter(val context: Context): RecyclerView.Adapter<NewFriendsListViewHolder>() {
    private var newFriendsList = ArrayList<FriendsItem>() // 친구 신청 현황 목록
    var onFriendsItemClickListener1: ((Int) -> Unit)? = null // 수락 버튼 클릭
    var onFriendsItemClickListener2: ((Int) -> Unit)? = null // 거절 버튼 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewFriendsListViewHolder {
        val binding: ItemFriendsBoxRvBinding = ItemFriendsBoxRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewFriendsListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = newFriendsList.size

    override fun onBindViewHolder(holder: NewFriendsListViewHolder, position: Int) {
        holder.bind(newFriendsList[position])

        // 친구 신청 수락 클릭 이벤트
        holder.binding.tvFriendsConfirm.setOnClickListener {
            onFriendsItemClickListener1?.invoke(position)
        }
        // 친구 신청 거절 클릭 이벤트
        holder.binding.tvFriendsRefuse.setOnClickListener {
            onFriendsItemClickListener2?.invoke(position)
        }
    }

    // 친구 신청 목록 추가
    fun addNewFriendsList(friendsItem: FriendsItem) {
        newFriendsList.add(friendsItem)
        notifyItemChanged(newFriendsList.size)
    }

    // 친구 리스트 반환
    fun getNewFriend(position: Int): FriendsItem = newFriendsList[position]

    // 친구 신청 목록 삭제
    fun removeFriend(position: Int) {
        newFriendsList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, newFriendsList.size - position)
    }
}