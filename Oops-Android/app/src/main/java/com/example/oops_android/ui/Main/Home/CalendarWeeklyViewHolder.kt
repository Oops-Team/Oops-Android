package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.R
import com.example.oops_android.databinding.ItemHomeCalendarBinding

// 홈 화면의 주간 캘린더에 들어갈 RV 뷰 홀더
class CalendarWeeklyViewHolder(val context: Context, val binding: ItemHomeCalendarBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CalendarIWeeklytem) {

        // 날짜 0 제거
        var tempDate: String? = item.date
        if (item.date[0].toString() == "0") {
            tempDate = "  " + item.date[1].toString() + "  "
        }

        // 선택된 날짜라면
        if (item.isSelected) {
            // 레이아웃 보이기
            binding.fLayoutHomeCalendarSelected.visibility = View.VISIBLE
            binding.fLayoutHomeCalendarDefault.visibility = View.GONE

            // 데이터 넣기
            binding.tvHomeCalendarDaySelected.text = item.day // 요일
            binding.tvHomeCalendarDateSelected.text = tempDate // 날짜
            item.isSelected = true
        }
        else { // 기본 값 날짜
            // 레이아웃 보이기
            binding.fLayoutHomeCalendarSelected.visibility = View.GONE
            binding.fLayoutHomeCalendarDefault.visibility = View.VISIBLE

            // 데이터 넣기
            binding.tvHomeCalendarDayDefault.text = item.day // 요일
            binding.tvHomeCalendarDateDefault.text = tempDate // 날짜
        }
    }
}