package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.util.Log
import androidx.activity.OnBackPressedCallback
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentHomeWeeklyBinding
import com.example.oops_android.ui.BaseFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class HomeWeeklyFragment: BaseFragment<FragmentHomeWeeklyBinding>(FragmentHomeWeeklyBinding::inflate) {

    // 뒤로가기 콜백
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 핸드폰 뒤로가기 이벤트
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.getBackPressedEvent()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove() // 콜백 제거
    }

    override fun initViewCreated() {
        // 주간 달력 어댑터
        val adapter = CalendarWeeklyAdapter(requireContext())

        // 달력 내의 아이템 중앙정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvHomeCalendar.layoutManager = it
            binding.rvHomeCalendar.adapter = adapter
        }

        // 요일 배열
        val weekDayList: Array<String> = resources.getStringArray(R.array.calendar_day_array)

        // 일주일 날짜 구하기(일요일부터)
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-mm-dd"))
        Log.d("오늘 날짜", today)
        val fullDateFormate = DateTimeFormatter.ofPattern("yyyy-mm-dd")
        val dateFormate = DateTimeFormatter.ofPattern("dd")
        val weekDate: LocalDateTime = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
        for (i in 0..6) {
            var isSelected = false
            val fullDate = weekDate.plusDays(i.toLong()).format(fullDateFormate)
            val date = weekDate.plusDays(i.toLong()).format(dateFormate)

            // 오늘날짜라면 true
            if (fullDate == today)
                isSelected = true

            // 값 넣기
            adapter.addWeeklyList(CalendarIWeeklytem(weekDayList[i], date, fullDate, isSelected))
        }

        // 날짜 클릭 이벤트
        adapter.onItemClickListener = { position ->
            adapter.setDateSelected(position)
        }
    }

    override fun initAfterBinding() {

    }
}