package com.oops.oops_android.ui.Main.Inventory

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemInventoryStuffBoxRvBinding
import com.oops.oops_android.ui.Main.Home.StuffItem

/* 인벤토리 리스트 RV 뷰 홀더 */
class InventoryStuffListViewHolder(val binding: ItemInventoryStuffBoxRvBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StuffItem) {
        // 데이터 적용
        val stuffImg = binding.itemInventoryStuffImageIv
        val stuffName = binding.itemInventoryStuffTitleTv

        Glide.with(applicationContext())
            .load(item.stuffImgUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.color.White)
            .into(stuffImg)

        stuffName.text = item.stuffName
    }
}
