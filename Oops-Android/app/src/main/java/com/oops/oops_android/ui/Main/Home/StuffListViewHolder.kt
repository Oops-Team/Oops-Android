package com.oops.oops_android.ui.Main.Home

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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

        // 선택한 아이템이라면
        if (item.isTakeStuff == true) {
            // 이미지를 흑백으로 변경
            val matrix = ColorMatrix()
            matrix.setSaturation(0F)
            val colorFilter = ColorMatrixColorFilter(matrix)
            stuffImg.colorFilter = colorFilter

            stuffName.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.Gray_50))
            stuffName.setTextAppearance(R.style.Button3)
            stuffName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.Gray_400)))
        }
        // 선택하지 않은 아이템이라면
        else {
            stuffImg.clearColorFilter()

            stuffName.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.Main_400))
            stuffName.setTextAppearance(R.style.Button3_Active)
            stuffName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.White)))
        }
    }
}
