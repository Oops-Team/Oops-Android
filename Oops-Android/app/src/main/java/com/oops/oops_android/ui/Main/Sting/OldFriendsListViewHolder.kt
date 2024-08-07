package com.oops.oops_android.ui.Main.Sting

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemFriendsBoxRv2Binding

class OldFriendsListViewHolder(val context: Context, val binding: ItemFriendsBoxRv2Binding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FriendsItem) {
        // 데이터 적용
        val friendImg = binding.ivFriendsProfile2
        val friendName = binding.tvFriendsName2

        Glide.with(context)
            .load(item.userImg)
            .fallback(R.color.White)
            .error(R.drawable.ic_friends_profile_default_50)
            .into(friendImg)

        friendName.text = item.userName
    }
}
