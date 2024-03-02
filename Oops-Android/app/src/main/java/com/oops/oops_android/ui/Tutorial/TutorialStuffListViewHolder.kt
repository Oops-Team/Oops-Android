package com.oops.oops_android.ui.Tutorial

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemTutorialStuffBinding

// 튜토리얼3 화면의 소지품 목록 RV 뷰 홀더
class TutorialStuffListViewHolder(val context: Context, val binding: ItemTutorialStuffBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TutorialStuffItem) {
        // 데이터 적용
        //val stuffCv = binding.cvItemTutorialStuff
        val stuffImg = binding.ivItemTutorialStuff
        val stuffName = binding.tvItemTutorialStuff

        Glide.with(context)
            .load(item.stuffImgUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.color.White)
            .into(stuffImg)

        stuffName.text = item.stuffName

        // 각 뷰 사이즈를 해상도에 맞게 설정
        var deviceWidth = context.resources.displayMetrics.widthPixels // 가로
        deviceWidth = (deviceWidth - 32) / 3
        stuffImg.layoutParams.width = deviceWidth
        stuffImg.requestLayout()

        // 선택한 아이템이라면
        if (item.isSelected) {
            stuffImg.clearColorFilter()

            //stuffCv.setCardBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.Main_500)))
            stuffName.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.Main_400))
            stuffName.setTextAppearance(R.style.Button3_Active)
            stuffName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.White)))
        }
        // 선택하지 않은 아이템이라면
        else {
            // 이미지를 흑백으로 변경
            val matrix = ColorMatrix()
            matrix.setSaturation(0F)
            val colorFilter = ColorMatrixColorFilter(matrix)
            stuffImg.colorFilter = colorFilter

            //stuffCv.setCardBackgroundColor(ColorStateList.valueOf(context.getColor(R.color.Gray_50)))
            stuffName.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.Gray_50))
            stuffName.setTextAppearance(R.style.Button3)
            stuffName.setTextColor(ColorStateList.valueOf(context.getColor(R.color.Gray_400)))
        }
    }
}
