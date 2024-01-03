package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
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
    private var weeklyAdapter: CalendarWeeklyAdapter? = null // 주간 달력 어댑터
    private var stuffAdapter: StuffListAdapter? = null // 소지품 어댑터
    private var todoAdapter: TodoListAdapter? = null // 할 일 어댑터

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
        weeklyAdapter = CalendarWeeklyAdapter(requireContext())
        binding.rvHomeCalendar.adapter = weeklyAdapter

        // 달력 내의 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvHomeCalendar.layoutManager = it
            binding.rvHomeCalendar.adapter = weeklyAdapter
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
            weeklyAdapter?.addWeeklyList(CalendarIWeeklytem(weekDayList[i], date, fullDate, isSelected, isToday))
        }

        // 챙겨야 할 것 목록
        // TODO:: 현재는 임시 데이터 적용
        stuffAdapter = StuffListAdapter(requireContext())
        binding.rvHomeStuff.adapter = stuffAdapter
        stuffAdapter?.addStuffList(StuffItem(1, R.drawable.untitled, "책"))
        stuffAdapter?.addStuffList(StuffItem(2, R.drawable.untitled, "책"))
        stuffAdapter?.addStuffList(StuffItem(3, R.drawable.untitled, "책"))
        stuffAdapter?.addStuffList(StuffItem(4, R.drawable.untitled, "책"))
        stuffAdapter?.addStuffList(StuffItem(5, R.drawable.untitled, "책"))

        // 일정 목록
        todoAdapter = TodoListAdapter(requireContext())
        binding.rvHomeTodo.adapter = todoAdapter
        // TODO:: 임시 데이터 적용(list값이 있다면 뷰 띄우기)
        binding.lLayoutHomeTodoDefault.visibility = View.GONE
        binding.iBtnHomeTodoAdd.visibility = View.VISIBLE
        todoAdapter?.addTodoList(TodoItem(1, "일정 이름1", "태그1", false))
        todoAdapter?.addTodoList(TodoItem(2, "일정 이름2", "태그2", false))
        todoAdapter?.addTodoList(TodoItem(3, "일정 이름3", "태그3", true))
        todoAdapter?.addTodoList(TodoItem(4, "일정 이름4", "태그4", true))
        todoAdapter?.addTodoList(TodoItem(5, "일정 이름5", "태그5", true))
        todoAdapter?.addTodoList(TodoItem(6, "일정 이름6", "태그5", true))
        todoAdapter?.addTodoList(TodoItem(7, "일정 이름7", "태그5", true))
        todoAdapter?.addTodoList(TodoItem(8, "일정 이름8", "태그5", true))
        todoAdapter?.addTodoList(TodoItem(9, "일정 이름9", "태그5", true))
        todoAdapter?.addTodoList(TodoItem(10, "일정 이름10", "태그5", true))
    }

    override fun initAfterBinding() {
        // 날짜 클릭 이벤트
        weeklyAdapter?.onItemClickListener = { position ->
            weeklyAdapter?.setDateSelected(position)
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

        // 일정 추가 버튼 클릭 이벤트
        binding.iBtnHomeTodoAdd.setOnClickListener {

        }

        // 일정 수정&삭제 버튼 클릭 이벤트
        todoAdapter?.onItemClickListener = { position, iv ->
            showEditPopup(position, iv)
        }

        // TODO:: 소지품 클릭 이벤트

    }


}
