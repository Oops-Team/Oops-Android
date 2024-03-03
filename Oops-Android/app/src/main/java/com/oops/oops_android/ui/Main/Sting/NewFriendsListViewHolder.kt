package com.oops.oops_android.ui.Main.Sting

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemFriendsBoxRvBinding

// 친구 신청 현황 목록 리스트 뷰 홀더
class NewFriendsListViewHolder(val context: Context, val binding: ItemFriendsBoxRvBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FriendsItem) {
        // 데이터 적용
        val friendImg = binding.ivFriendsProfile
        val friendName = binding.tvFriendsName

        Glide.with(context)
            .load(item.userImg)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_friends_profile_default_50)
            .into(friendImg)

        friendName.text = item.userName

        // code 유형에 따른 데이터 처리
        when (item.userState) {
            0L -> binding.tvFriendsAdd.visibility = View.VISIBLE // 친구x
            2L -> binding.tvFriendsWait.visibility = View.VISIBLE // 대기중
            3L -> binding.lLayoutFriendsBtn.visibility = View.VISIBLE // 친구 요청이 들어 온 경우
        }
    }
}
