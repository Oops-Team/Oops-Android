package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ItemTutorialStuffBinding
import com.oops.oops_android.ui.Main.Home.StuffItem
import kotlin.math.roundToInt

/* 소지품 수정 화면의 소지품 목록 RV 뷰 홀더 */
class StuffTodoListViewHolder(val binding: ItemTutorialStuffBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: StuffItem) {
        // 데이터 적용
        val stuffImg = binding.ivItemTutorialStuff
        val stuffName = binding.tvItemTutorialStuff

        Glide.with(applicationContext())
            .load(stuffImg) // fixme: 서버 이미지 연결 되면 수정하기
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.color.White)
            .into(stuffImg)

        stuffName.text = item.stuffName
        stuffName.setTextColor(ContextCompat.getColor(applicationContext(), R.color.White))
        stuffName.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext(), R.color.Main_400))

        // 각 뷰 사이즈를 해상도에 맞게 설정
        var deviceWidth = applicationContext().resources.displayMetrics.widthPixels // 가로
        deviceWidth = (deviceWidth - 32) / 3
        stuffImg.layoutParams.width = deviceWidth
        stuffImg.layoutParams.height = 200
        stuffImg.requestLayout()
    }
}
