package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemHomeCalendarBinding

// 홈 화면의 주간 캘린더에 들어갈 RV 어댑터
class CalendarWeeklyAdapter(val context: Context): RecyclerView.Adapter<CalendarWeeklyViewHolder>() {

    private var weeklyList = ArrayList<CalendarIWeeklytem>()
    // 날짜 클릭
    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarWeeklyViewHolder {
        val binding: ItemHomeCalendarBinding = ItemHomeCalendarBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CalendarWeeklyViewHolder(context, binding)
    }

    override fun getItemCount(): Int = weeklyList.size

    override fun onBindViewHolder(holder: CalendarWeeklyViewHolder, position: Int) {
        holder.bind(weeklyList[position])

        // 날짜 클릭
        holder.binding.lLayoutHomeCalendarTop.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    // 아이템 추가
    fun addWeeklyList(calendarIWeeklytem: CalendarIWeeklytem) {
        weeklyList.add(calendarIWeeklytem)
        notifyItemChanged(weeklyList.size)
    }

    // 아이템 반환
    fun getWeeklyList(position: Int): CalendarIWeeklytem {
        return weeklyList[position]
    }

    // 아이템 활성화&비활성화
    fun setDateSelected(position: Int) {
        // 기존에 선택되어 있던 아이템 비활성화
        for (i in 0 until weeklyList.size) {
            weeklyList[i].isSelected = false
            notifyItemChanged(i)
        }
        weeklyList[position].isSelected = true
        notifyItemChanged(position)
    }
}