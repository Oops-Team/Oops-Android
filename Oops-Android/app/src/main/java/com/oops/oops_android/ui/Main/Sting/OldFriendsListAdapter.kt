package com.oops.oops_android.ui.Main.Sting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemFriendsBoxRv2Binding
import com.oops.oops_android.utils.setOnSingleClickListener

class OldFriendsListAdapter(val context: Context): RecyclerView.Adapter<OldFriendsListViewHolder>() {
    private var oldFriendsList = ArrayList<FriendsItem>() // 친구 목록
    var onOldFriendsItemClickListener1: ((Int) -> Unit)? = null // 삭제 버튼 클릭
    var onOldFriendsItemClickListener2: ((Int) -> Unit)? = null // 찌르기 버튼 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OldFriendsListViewHolder {
        val binding: ItemFriendsBoxRv2Binding = ItemFriendsBoxRv2Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OldFriendsListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = oldFriendsList.size

    override fun onBindViewHolder(holder: OldFriendsListViewHolder, position: Int) {
        holder.bind(oldFriendsList[position])

        // 친구 삭제 버튼 클릭 이벤트
        holder.binding.tvFriendsDelete.setOnSingleClickListener {
            onOldFriendsItemClickListener1?.invoke(position)
        }
        // 친구 찌르기 버튼 클릭 이벤트
        holder.binding.iBtnFriendsSting.setOnClickListener {
            onOldFriendsItemClickListener2?.invoke(position)
        }
    }

    // 친구 목록 추가
    fun addOldFriendsList(friendsItem: FriendsItem) {
        oldFriendsList.add(friendsItem)
        notifyItemChanged(oldFriendsList.size)
    }

    // 친구 리스트 반환
    fun getOldFriend(position: Int): FriendsItem = oldFriendsList[position]

    // 친구 목록 삭제
    fun removeFriend(position: Int) {
        oldFriendsList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, oldFriendsList.size - position)
    }
}