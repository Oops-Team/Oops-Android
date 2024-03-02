package com.oops.oops_android.custom

import android.content.Context
import androidx.core.content.ContextCompat
import com.oops.oops_android.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

// 홈 화면의 월간 캘린더 - 오늘 날짜 디자인
class TodayDecorator(val context: Context): DayViewDecorator {

    private val today: CalendarDay // 오늘 날짜
        get() {
            return CalendarDay.today()
        }

    override fun shouldDecorate(day: CalendarDay?): Boolean = day?.equals(today)!!

    override fun decorate(view: DayViewFacade?) {
        // 이미지 넣기
        ContextCompat.getDrawable(context, R.drawable.ic_monthly_today)
            ?.let { view?.setBackgroundDrawable(it) }
    }
}