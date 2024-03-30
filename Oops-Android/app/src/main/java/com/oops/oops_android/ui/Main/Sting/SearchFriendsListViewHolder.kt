package com.oops.oops_android.ui.Main.Sting

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemFriendsBoxRv3Binding

class SearchFriendsListViewHolder(val binding: ItemFriendsBoxRv3Binding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FriendsItem) {
        // 데이터 적용
        val userImg = binding.ivFriendsBox3Profile
        val userName = binding.tvFriendsBox3Name

        Glide.with(applicationContext())
            .load(item.userImg)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_friends_profile_default_50)
            .into(userImg)

        userName.text = item.userName

        // 친구 유형에 따른 뷰 처리
        when (item.userState) {
            1 -> {
                // 친구임
                binding.iBtnFriendsBox3Sting.visibility = View.VISIBLE
            }
            2 -> {
                // 대기 중
                binding.tvFriendsBox3Wait.visibility = View.VISIBLE
            }
            3 -> {
                // 친구 요청이 들어 온 경우
                binding.lLayoutFriendsBox3Btn.visibility = View.VISIBLE
            }
            else -> {
                // 친구 아님
                binding.tvFriendsBox3Add.visibility = View.VISIBLE
            }
        }
    }
}
