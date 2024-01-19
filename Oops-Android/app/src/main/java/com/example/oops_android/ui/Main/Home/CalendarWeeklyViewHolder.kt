package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.R
import com.example.oops_android.databinding.ItemHomeCalendarBinding

// 홈 화면의 주간 캘린더에 들어갈 RV 뷰 홀더
class CalendarWeeklyViewHolder(val context: Context, val binding: ItemHomeCalendarBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CalendarIWeeklytem) {

        // 날짜 0 제거 및 공백 설정
        val tempDate: String =
            if (item.date[0].toString() == "0") {
                "  " + item.date[1].toString() + "  "
            } else {
                " " + item.date + " "
            }

        // 선택된 날짜라면
        if (item.isSelected) {
            binding.fLayoutHomeCalendarDefault.setBackgroundResource(R.drawable.all_radius_50)
            binding.fLayoutHomeCalendarDefault.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.Main_500)
            )
            binding.tvHomeCalendarDateDefault.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.White)))
            binding.tvHomeCalendarDayDefault.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.White)))
        }
        // 기본 날짜라면
        else {
            binding.fLayoutHomeCalendarDefault.setBackgroundResource(R.drawable.all_radius_50)
            binding.fLayoutHomeCalendarDefault.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.White)
            )
            binding.tvHomeCalendarDateDefault.setTextColor(Color.parseColor("#333333"))
            binding.tvHomeCalendarDayDefault.setTextColor(Color.parseColor("#707070"))
        }

        // 오늘 날짜라면
        binding.fLayoutHomeCalendarDefaultTop.setBackgroundResource(R.color.White)
        if (item.isToday && item.isSelected) {
            binding.fLayoutHomeCalendarDefaultTop.setBackgroundResource(R.drawable.home_calendar_today)
        }
        else if (item.isToday) {
            binding.fLayoutHomeCalendarDefaultTop.setBackgroundResource(R.drawable.home_calendar_today)
            binding.fLayoutHomeCalendarDefault.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.transparent)
            )
        }

        // 데이터 넣기
        binding.tvHomeCalendarDayDefault.text = item.day // 요일
        binding.tvHomeCalendarDateDefault.text = tempDate // 날짜
    }
}