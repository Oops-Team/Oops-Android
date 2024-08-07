package com.oops.oops_android.ui.Main.Home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemHomeStuffBinding

/* 홈 화면의 챙겨야 할 것 RV 뷰 홀더 */
class StuffListViewHolder(val context: Context, val binding: ItemHomeStuffBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StuffItem) {
        // 데이터 적용
        val stuffImg = binding.ivHomeStuff
        val stuffName = binding.tvHomeStuffName

        Glide.with(context)
            .load(item.stuffImgUrl)
            .fallback(R.color.White)
            .error(R.color.White)
            .into(stuffImg)

        stuffName.text = item.stuffName
        //stuffImg.setImageResource(item.stuffImgUrl)
    }
}
