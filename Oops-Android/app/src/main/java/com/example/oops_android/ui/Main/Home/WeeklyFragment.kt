package com.example.oops_android.ui.Main.Home

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.oops_android.R
import com.example.oops_android.custom.EventDecorator
import com.example.oops_android.custom.SelectedDecorator
import com.example.oops_android.custom.TodayDecorator
import com.example.oops_android.databinding.FragmentWeeklyBinding
import com.example.oops_android.ui.Base.BaseFragment
import com.example.oops_android.utils.CalendarUtils
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 홈 화면(주간 캘린더: default)
class WeeklyFragment: BaseFragment<FragmentWeeklyBinding>(FragmentWeeklyBinding::inflate) {

    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백
    private var weeklyAdapter: CalendarWeeklyAdapter? = null // 주간 달력 어댑터
    private var stuffAdapter: StuffListAdapter? = null // 소지품 어댑터
    private var todoAdapter: TodoListAdapter? = null // 할 일 어댑터
    private var isWeeklyCalendar: Boolean = true // 주간&월간 캘린더 전환 변수

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
        // 바텀 네비게이션 보이기
        mainActivity?.hideBnv(false)

        // 주간 달력 어댑터
        weeklyAdapter = CalendarWeeklyAdapter(requireContext())
        binding.rvHomeWeeklyCalendar.adapter = weeklyAdapter

        // 달력 내의 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvHomeWeeklyCalendar.layoutManager = it
            binding.rvHomeWeeklyCalendar.adapter = weeklyAdapter
        }

        // 요일 배열
        val weekDayList: Array<String> = resources.getStringArray(R.array.calendar_week_array)

        // 날짜 포맷
        val fullDateFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormate = DateTimeFormatter.ofPattern("dd")

        // 오늘 날짜 가져오기
        val today: String = CalendarUtils.getTodayDateList()[0].toString()

        // 일주일의 시작 날짜 가져오기
        val weekDate = CalendarUtils.getTodayDateList()[1] as LocalDateTime

        for (i in 0..6) {
            var isSelected = false
            var isToday = false
            val fullDate = weekDate.plusDays(i.toLong()).format(fullDateFormate)
            val date = weekDate.plusDays(i.toLong()).format(dateFormate)

            // 오늘 날짜라면 true
            if (fullDate == today) {
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
        stuffAdapter?.addStuffList(StuffItem(1, R.drawable.ex_head, "헤드셋"))
        stuffAdapter?.addStuffList(StuffItem(2, R.drawable.ex_wallet, "지갑"))
        stuffAdapter?.addStuffList(StuffItem(3, R.drawable.ex_keyboard, "키보드"))
        stuffAdapter?.addStuffList(StuffItem(4, R.drawable.ex_diary, "다이어리"))

        // 일정 목록
        todoAdapter = TodoListAdapter(requireContext())
        binding.rvHomeTodo.adapter = todoAdapter
        // TODO:: 임시 데이터 적용(list값이 있다면 뷰 띄우기)
        binding.lLayoutHomeTodoDefault.visibility = View.GONE
        binding.iBtnHomeTodoAdd.visibility = View.VISIBLE
        todoAdapter?.addTodoList(TodoItem(1, "일정 이름1", false))
        todoAdapter?.addTodoList(TodoItem(2, "일정 이름2", false))
        todoAdapter?.addTodoList(TodoItem(3, "일정 이름3", true))
        todoAdapter?.addTodoList(TodoItem(4, "일정 이름4", true))
        todoAdapter?.addTodoList(TodoItem(5, "일정 이름5", true))
        todoAdapter?.addTodoList(TodoItem(6, "일정 이름6", true))
        todoAdapter?.addTodoList(TodoItem(7, "일정 이름7", true))
        todoAdapter?.addTodoList(TodoItem(8, "일정 이름8", true))
        todoAdapter?.addTodoList(TodoItem(9, "일정 이름9", true))
        todoAdapter?.addTodoList(TodoItem(10, "일정 이름10", true))

        // 월간 캘린더
        binding.mcvHomeMonthlyCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기
        setCalendarTodoState()

        // 오늘 할 일 태그 동적 생성
        val tagList = arrayListOf<String>()
        tagList.add("일상")
        tagList.add("취미")
        tagList.add("운동")

        setTodoTag(tagList)
    }

    override fun initAfterBinding() {
        // 주간 날짜 클릭 이벤트
        weeklyAdapter?.onItemClickListener = { position ->
            weeklyAdapter?.setDateSelected(position)
        }

        // 월간 캘린더 - 오늘 날짜 표시
        val todayDecorator = TodayDecorator(requireContext())
        binding.mcvHomeMonthlyCalendarview.addDecorators(todayDecorator)

        // 월간 캘린더 - 날짜 클릭 이벤트
        binding.mcvHomeMonthlyCalendarview.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = binding.mcvHomeMonthlyCalendarview.selectedDate!!
            val eventDecorator = SelectedDecorator(requireContext(), selectedDate)
            binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
        }

        // 월간 캘린더 - 달이 바뀔 때마다 dot 바꿔주기
        binding.mcvHomeMonthlyCalendarview.setOnMonthChangedListener { widget, date ->

        }

        // 캘린더 버튼 클릭 이벤트
        binding.ivHomeSwitchCalendar.setOnClickListener {
            if (isWeeklyCalendar) {
                binding.rvHomeWeeklyCalendar.visibility = View.GONE
                binding.mcvHomeMonthlyCalendarview.visibility = View.VISIBLE
                binding.lLayoutWeeklyWeek.visibility = View.VISIBLE
                isWeeklyCalendar = false
            }
            else {
                binding.rvHomeWeeklyCalendar.visibility = View.VISIBLE
                binding.mcvHomeMonthlyCalendarview.visibility = View.GONE
                binding.lLayoutWeeklyWeek.visibility = View.GONE
                isWeeklyCalendar = true
            }
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

        // 일정 추가 버튼(하단 버튼) 클릭 이벤트
        binding.iBtnHomeTodoAdd.setOnClickListener {
            val actionToTodo: NavDirections = WeeklyFragmentDirections.actionHomeWeeklyFrmToTodoFrm()
            findNavController().navigate(actionToTodo)
        }

        // 일정 추가 버튼(상단 버튼) 클릭 이벤트
        binding.ivHomeTodoAdd.setOnClickListener {
            val actionToTodo: NavDirections = WeeklyFragmentDirections.actionHomeWeeklyFrmToTodoFrm()
            findNavController().navigate(actionToTodo)
        }

        // 일정 수정&삭제 버튼 클릭 이벤트
        todoAdapter?.onItemClickListener = { itemPos, iv, tv, edt ->
            showEditPopup(itemPos, iv, tv, edt)
        }

        // TODO:: 소지품 클릭 이벤트

    }

    // 월간 캘린더 - 일정 상태 표시
    private fun setCalendarTodoState() {
        val dates = mutableListOf<CalendarDay>()
        dates.add(CalendarDay.today())
        dates.add(CalendarDay.from(2024, 1, 24))
        val eventDecorator = EventDecorator(requireContext(), R.color.Main_100, dates)
        binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
    }

    // 오늘 할 일 태그 구현 함수
    @SuppressLint("InflateParams")
    private fun setTodoTag(tagList: List<String>) {
        // 객체 생성
        for (i in tagList.indices) {
            // 커스텀 뷰 가져오기
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.item_home_todo_tag, null)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            params.marginEnd = 12
            layout.findViewById<LinearLayout>(R.id.lLayout_home_todo_tag).layoutParams = params

            // 동적 textview 생성
            val tagView = layout.findViewById<TextView>(R.id.tv_home_todo_tag)
            tagView.text = tagList[i]

            // 뷰에 동적 layout 띄우기
            binding.lLayoutHomeTodoTag.addView(layout)
        }
    }

    // 수정 팝업 띄우기(홈 화면의 오늘 할 일에서 사용)
    private fun showEditPopup(itemPos: Int, iv: ImageView, tv: TextView, edt: EditText) {
        val popup = layoutInflater.inflate(R.layout.item_home_todo_popup, null)

        // popupwindow 생성
        val popupWindow = PopupWindow(popup, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true // 팝업 바깥 영역 클릭시 팝업 닫침
        popupWindow.showAsDropDown(iv, -210, 0) // ... 아래에 팝업 위치하도록 함

        // 팝업 배경 뒤 흐리게
        val container: View = popupWindow.contentView.parent as View
        val windowManager: WindowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val params: WindowManager.LayoutParams = container.layoutParams as WindowManager.LayoutParams
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0.25f
        windowManager.updateViewLayout(container, params)

        // 수정 버튼 클릭 이벤트
        val editBtn: LinearLayout = popup.findViewById(R.id.lLayout_home_todo_edit_popup)
        editBtn.setOnClickListener {
            popupWindow.dismiss()
            // 선택한 아이템 정보 가져오기
            val todoItem: TodoItem? = todoAdapter?.getTodoList(itemPos)

            // tv -> edt 전환
            tv.visibility = View.INVISIBLE
            edt.visibility = View.VISIBLE

            edt.apply {
                setText(todoItem?.todoName) // 정보 넣기
                setSelection(edt.length()) // 포커스 마지막에 주기

                // 딜레이를 주어 키보드 띄우기
                postDelayed({
                    getShowKeyboard(edt)
                }, 300)
            }

            // 사용자가 완료 버튼을 클릭했다면
            edt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        getEditDone(edt, tv, itemPos)
                        return true
                    }
                    return false
                }
            })

            // 사용자가 입력 도중 바깥을 클릭했다면
            binding.lLayoutHomeInside.setOnClickListener {
                getEditDone(edt, tv, itemPos)
            }
        }

        // 삭제 버튼 클릭 이벤트
        val deleteBtn: LinearLayout = popup.findViewById(R.id.lLayout_home_todo_delete_popup)
        deleteBtn.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    // 일정 수정 완료 이벤트
    private fun getEditDone(edt: EditText, tv: TextView, itemPos: Int) {
        // 키보드 내리기
        getHideKeyboard(binding.root)

        // edt -> tv 전환
        edt.visibility = View.INVISIBLE
        tv.visibility = View.VISIBLE

        // 입력된 값이 있다면
        if (edt.text.isNotEmpty()) {
            // 입력한 값 저장 및 아이템 값 수정
            tv.text = edt.text.toString()
            todoAdapter?.modifyTodoList(itemPos, edt.text.toString())

            // TODO: API에 저장
        }
    }
}
