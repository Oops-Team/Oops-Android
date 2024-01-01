package com.example.oops_android.ui.Main.Home

import android.content.Context
import androidx.activity.OnBackPressedCallback
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentWeeklyBinding
import com.example.oops_android.ui.BaseFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.TimeZone


// 홈 화면(주간 캘린더: default)
class WeeklyFragment: BaseFragment<FragmentWeeklyBinding>(FragmentWeeklyBinding::inflate) {

    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백
    private var adapter: CalendarWeeklyAdapter? = null // 주간 달력 어뎁터

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
        adapter = CalendarWeeklyAdapter(requireContext())
        binding.rvHomeCalendar.adapter = adapter

        // 달력 내의 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvHomeCalendar.layoutManager = it
            binding.rvHomeCalendar.adapter = adapter
        }

        // 요일 배열
        val weekDayList: Array<String> = resources.getStringArray(R.array.calendar_day_array)

        // 날짜 포맷
        val fullDateFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormate = DateTimeFormatter.ofPattern("dd")

        // 각 기기에 따른 오늘 날짜 구하기
        val instant = Instant.now()
        val zonedDateTime: ZonedDateTime = instant.atZone(TimeZone.getDefault().toZoneId())

        // 오늘 날짜 기준으로
        val todayList = zonedDateTime.toString().split("T") // 년도-월-일만 분리
        val todayFormatList = zonedDateTime.toString().split(".") // LocalDateTime 포맷으로 분리
        val localDateTime: LocalDateTime = LocalDateTime.parse(todayFormatList[0]) // LocalDateTime으로 만들기
        val weekDate: LocalDateTime? = localDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)) // 일주일 시작 날짜 구하기

        for (i in 0..6) {
            var isSelected = false
            var isToday = false
            val fullDate = weekDate!!.plusDays(i.toLong()).format(fullDateFormate)
            val date = weekDate.plusDays(i.toLong()).format(dateFormate)

            // 오늘 날짜라면 true
            if (fullDate == todayList[0]) {
                isSelected = true
                isToday = true
            }

            // 값 넣기
            adapter?.addWeeklyList(CalendarIWeeklytem(weekDayList[i], date, fullDate, isSelected, isToday))
        }
    }

    override fun initAfterBinding() {
        // 날짜 클릭 이벤트
        adapter?.onItemClickListener = { position ->
            adapter?.setDateSelected(position)
        }

        // 수정 버튼 클릭 이벤트
        binding.ivHomeEdit.setOnClickListener {
            val dialog = EditDialog(requireContext(), mainActivity!!)
            dialog.editDialogShow()

            dialog.setOnClickedListener(object : EditDialog.ButtonClickListener {
                override fun onClicked() {
                    // TODO 소지품 수정 or 할일 수정
                }

            })
        }
    }
}
