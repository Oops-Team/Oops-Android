package com.example.oops_android.custom

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.oops_android.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

// 홈 화면의 월간 캘린더 - 선택한 날짜 디자인
class SelectedDecorator(private val context: Context, private val date: CalendarDay?): DayViewDecorator {

    // 선택한 날짜(true인 날짜값)만 이미지 적용하기
    override fun shouldDecorate(day: CalendarDay?): Boolean = day?.equals(date)!!

    override fun decorate(view: DayViewFacade?) {
        // 이미지 넣기
        ContextCompat.getDrawable(context, R.drawable.selector_monthly_calendar_event)
            ?.let { view?.setSelectionDrawable(it) }
    }
}