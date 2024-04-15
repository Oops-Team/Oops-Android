package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemTutorialStuffBinding

/* 소지품 추가 화면의 소지품 목록 RV 뷰 홀더 */
class StuffAddListViewHolder(val binding: ItemTutorialStuffBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StuffAddItem) {
        // 데이터 적용
        val stuffImg = binding.ivItemTutorialStuff
        val stuffName = binding.tvItemTutorialStuff
        val stuffLayout = binding.lLayoutItemTutorialTop

        Glide.with(applicationContext())
            .load(item.stuffImgUrl)
            .fallback(R.color.White)
            .error(R.color.White)
            .into(stuffImg)

        stuffName.text = item.stuffName

        // 각 뷰 사이즈를 해상도에 맞게 설정
        var deviceWidth = applicationContext().resources.displayMetrics.widthPixels // 가로
        deviceWidth = (deviceWidth - 32) / 3
        stuffImg.layoutParams.width = deviceWidth
        //stuffImg.layoutParams.height = (deviceWidth / 1.5).toInt()
        stuffImg.requestLayout()

        // 선택한 소지품이라면
        if (item.isSelected) {
            stuffImg.clearColorFilter()

            stuffLayout.backgroundTintList = ColorStateList.valueOf(applicationContext().getColor(R.color.Main_50))
            stuffName.backgroundTintList = ColorStateList.valueOf(applicationContext().getColor(R.color.Main_400))
            stuffName.setTextAppearance(R.style.Button3_Active)
            stuffName.setTextColor(ColorStateList.valueOf(applicationContext().getColor(R.color.White)))
        }
        // 선택하지 않은 소지품이라면
        else {
            // 이미지를 흑백으로 변경
            val matrix = ColorMatrix()
            matrix.setSaturation(0F)
            val colorFilter = ColorMatrixColorFilter(matrix)
            stuffImg.colorFilter = colorFilter

            stuffLayout.backgroundTintList = ColorStateList.valueOf(applicationContext().getColor(R.color.White))
            stuffName.backgroundTintList = ColorStateList.valueOf(applicationContext().getColor(R.color.Gray_50))
            stuffName.setTextAppearance(R.style.Button3)
            stuffName.setTextColor(ColorStateList.valueOf(applicationContext().getColor(R.color.Gray_400)))
        }
    }
}
