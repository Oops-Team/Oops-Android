package com.example.oops_android.custom

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

// 홈 화면의 월간 캘린더 - 한달 단위로 날짜에 대하여 decorator 넣어주기
class EventDecorator(
    val context: Context,
    private val eventColor: Int,
    private val dates: MutableList<CalendarDay>
): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean = dates.contains(day)

    override fun decorate(view: DayViewFacade?) {
        // 각 색상에 맞춰서 날짜 밑에 dot 찍기
        view?.addSpan(DotSpan(6F, ContextCompat.getColor(context, eventColor)))
    }
}