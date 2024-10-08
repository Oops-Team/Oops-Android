package com.oops.oops_android.ui.Main.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.InputType
import android.util.Log
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.oops.oops_android.R
import com.oops.oops_android.custom.EventDecorator
import com.oops.oops_android.custom.SelectedDecorator
import com.oops.oops_android.custom.TodayDecorator
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.CalendarUtils
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.oops.oops_android.ApplicationClass
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Todo.Api.TodoMonthlyView
import com.oops.oops_android.data.remote.Todo.Api.TodoService
import com.oops.oops_android.data.remote.Todo.Api.TodoView
import com.oops.oops_android.data.remote.Todo.Model.StuffDeleteModel
import com.oops.oops_android.data.remote.Todo.Model.TodoDeleteAllModel
import com.oops.oops_android.data.remote.Todo.Model.TodoModifyModel
import com.oops.oops_android.databinding.FragmentHomeBinding
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.CalendarUtils.Companion.getTodayDate
import com.oops.oops_android.utils.setOnSingleClickListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// 홈 화면(주간 캘린더: default)
class HomeFragment:
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    TodoView,
    CommonView,
    TodoMonthlyView {

    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백
    private var weeklyAdapter: CalendarWeeklyAdapter? = null // 주간 달력 어댑터
    private var stuffAdapter: StuffListAdapter? = null // 소지품 어댑터
    private var todoAdapter: TodoListAdapter? = null // 할 일 어댑터
    private var isWeeklyCalendar: Boolean = true // 주간&월간 캘린더 전환 변수
    private lateinit var selectDate: LocalDate // 현재 사용자가 선택 중인 날짜

    /* API에서 불러와 저장하는 데이터 */
    private var todoListItem: TodoListItem? = null // 오늘 날짜의 데이터 리스트(일정 수정 화면에 넘겨주기 위함)
    private var inventoryList = ArrayList<HomeInventoryItem>() // 전체 인벤토리 리스트
    private var monthlyItemList = ArrayList<MonthlyItem>() // 월간 캘린더 아이템 리스트

    // 클릭했던 소지품 리스트
    private var clickedStuffList = ArrayList<String>()

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

        // 주간 캘린더 어댑터
        weeklyAdapter = CalendarWeeklyAdapter(requireContext())
        binding.rvHomeWeeklyCalendar.adapter = weeklyAdapter

        // 주간 캘린더 내의 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvHomeWeeklyCalendar.layoutManager = it
            binding.rvHomeWeeklyCalendar.adapter = weeklyAdapter
        }

        // 월간 캘린더
        binding.mcvHomeMonthlyCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기

        // 선택되어 있는 날짜 설정
        selectDate = LocalDate.parse(getTodayDate().toString().split("T")[0])
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initAfterBinding() {
        // 뷰 초기화
        binding.mcvHomeMonthlyCalendarview.visibility = View.GONE
        binding.rvHomeWeeklyCalendar.visibility = View.VISIBLE
        binding.lLayoutWeeklyWeek.visibility = View.GONE
        todoAdapter?.resetTodoList()
        stuffAdapter?.resetStuffList()
        binding.lLayoutHomeTodoTag.removeAllViews()
        todoListItem = null
        isWeeklyCalendar = true

        // 일정 어댑터 연결
        todoAdapter = TodoListAdapter(requireContext())
        binding.rvHomeTodo.adapter = todoAdapter

        // 챙겨야 할 것 리스트 어댑터 연결
        stuffAdapter = StuffListAdapter(requireContext())
        binding.rvHomeStuff.adapter = stuffAdapter

        // 선택된 날짜의 일주일 시작 날짜 정보 찾기
        val weekDate = CalendarUtils.getStartDate(LocalDate.of(selectDate.year, selectDate.month, selectDate.dayOfMonth))

        // 현재 선택되어 있는 날짜 정보를 주간 캘린더에 적용하기
        weeklyAdapter?.resetWeeklyList() // 리스트 초기화
        setWeeklyCalendar(weekDate) // 주간 캘린더 날짜 설정
        val date = LocalDate.of(selectDate.year, selectDate.month, selectDate.dayOfMonth)
        weeklyAdapter?.setDateSelected(weeklyAdapter!!.getItemIndex(date.toString())) // 선택된 날짜 설정

        // 일정 1개 조회 API 연결(전체 인벤토리 리스트, 일정 리스트, 챙겨야 할 것 리스트 불러오기)
        getTodo(selectDate)

        // 월간 캘린더 - 오늘 날짜 표시
        val todayDecorator = TodayDecorator(requireContext())
        binding.mcvHomeMonthlyCalendarview.addDecorators(todayDecorator)

        // 월간 캘린더 - 날짜 클릭 이벤트
        binding.mcvHomeMonthlyCalendarview.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = binding.mcvHomeMonthlyCalendarview.selectedDate!!
            val eventDecorator = SelectedDecorator(requireContext(), selectedDate)
            binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)

            // 날짜 선택 정보 업데이트
            selectDate = LocalDate.of(selectedDate.year, selectedDate.month, selectedDate.day)

            // 일정 1개 조회 API 연결
            getTodo(selectDate)
        }

        // 월간 캘린더 - 달이 바뀔 때마다 dot 바꿔주기
        binding.mcvHomeMonthlyCalendarview.setOnMonthChangedListener { widget, date ->
            // 기존에 설정되어 있던 decorators 초기화
            /*binding.mcvHomeMonthlyCalendarview.removeDecorators()
            binding.mcvHomeMonthlyCalendarview.invalidateDecorators()*/

            // 일정 전체(1달) 조회 API 연결
            getMonthlyTodo(LocalDate.of(date.year, date.month, date.day))
        }

        // 주간 날짜 클릭 이벤트
        weeklyAdapter?.onItemClickListener = { position ->
            weeklyAdapter?.setDateSelected(position)

            // 날짜 선택 정보 업데이트
            val todoDate = LocalDate.parse(weeklyAdapter?.getSelectedDate()?.fullDate)
            selectDate = todoDate

            // 일정 1개 조회 API 연결
            getTodo(todoDate)
        }

        // 캘린더 버튼 클릭 이벤트
        binding.ivHomeSwitchCalendar.setOnClickListener {
            // 주간 -> 월간
            if (isWeeklyCalendar) {
                binding.rvHomeWeeklyCalendar.visibility = View.GONE
                binding.mcvHomeMonthlyCalendarview.visibility = View.VISIBLE
                binding.lLayoutWeeklyWeek.visibility = View.VISIBLE
                isWeeklyCalendar = false

                // 주간 캘린더 선택된 날짜 정보 가져오기
                val fullDate = weeklyAdapter?.getSelectedDate()?.fullDate
                val dateFormatList = fullDate.toString().split("-") // 정보 분리

                // 주간 캘린더에서 선택된 날짜 정보를 월간 캘린더의 dot에 적용
                binding.mcvHomeMonthlyCalendarview.selectedDate = CalendarDay.from(dateFormatList[0].toInt(), dateFormatList[1].toInt(), dateFormatList[2].toInt())
                val decorator = SelectedDecorator(requireContext(), binding.mcvHomeMonthlyCalendarview.selectedDate)
                binding.mcvHomeMonthlyCalendarview.addDecorator(decorator)

                // 월간 캘린더에 선택된 날짜 적용
                binding.mcvHomeMonthlyCalendarview.currentDate = binding.mcvHomeMonthlyCalendarview.selectedDate

                // 일정 전체(1달) 조회 API 연동
                getMonthlyTodo(LocalDate.parse(fullDate))
            }
            // 월간 -> 주간
            else {
                binding.rvHomeWeeklyCalendar.visibility = View.VISIBLE
                binding.mcvHomeMonthlyCalendarview.visibility = View.GONE
                binding.lLayoutWeeklyWeek.visibility = View.GONE
                isWeeklyCalendar = true

                // 선택되어 있는 날짜 정보 가져오기
                val fullDate = binding.mcvHomeMonthlyCalendarview.selectedDate!!

                // 선택된 날짜의 일주일 시작 날짜 정보 찾기
                val weekDate = CalendarUtils.getStartDate(LocalDate.of(fullDate.year, fullDate.month, fullDate.day))

                // 현재 선택되어 있는 날짜 정보를 주간 캘린더에 적용하기
                weeklyAdapter?.resetWeeklyList() // 리스트 초기화
                setWeeklyCalendar(weekDate) // 주간 캘린더 날짜 설정
                val date = LocalDate.of(fullDate.year, fullDate.month, fullDate.day)
                weeklyAdapter?.setDateSelected(weeklyAdapter!!.getItemIndex(date.toString())) // 선택된 날짜 설정

                // 일정 1개 조회 API 연결
                getTodo(date)
            }
        }

        // 수정 버튼 클릭 이벤트
        binding.ivHomeEdit.setOnClickListener {
            val dialog = EditDialog(requireContext(), mainActivity!!)
            dialog.editDialogShow()

            dialog.setOnClickedListener(object : EditDialog.ButtonClickListener {
                override fun onClicked(isStuffEdit: Boolean) {
                    // 소지품 수정 버튼을 눌렀다면
                    if (isStuffEdit) {
                        // 소지품 수정 화면으로 이동
                        val actionToStuff: NavDirections = HomeFragmentDirections.actionHomeFrmToStuffFrm(
                            selectDate.toString(),
                            inventoryList.toTypedArray(),
                            stuffAdapter?.getStuffList()!!.toTypedArray()
                        )
                        findNavController().navigate(actionToStuff)
                    }
                    // 할일 수정을 눌렀다면
                    else {
                        // 일정 수정 화면으로 이동
                        val actionToTodo: NavDirections = HomeFragmentDirections.actionHomeFrmToTodoFrm(selectDate.toString(), todoListItem)
                        findNavController().navigate(actionToTodo)
                    }
                }
            })
        }

        // 일정 추가 버튼(하단 버튼) 클릭 이벤트
        binding.iBtnHomeTodoAdd.setOnClickListener {
            // 일정이 30개 이상이라면
            if (todoAdapter?.itemCount!! >= 30) {
                showCustomSnackBar(getString(R.string.toast_todo_not_add)) // 스낵바 띄우기
            }
            else {
                // 뷰 추가하기
                addTodoView()
            }
        }

        // 일정 추가 버튼(상단 버튼) 클릭 이벤트
        binding.ivHomeTodoAdd.setOnClickListener {
            val actionToTodo: NavDirections = HomeFragmentDirections.actionHomeFrmToTodoFrm(selectDate.toString(), todoListItem)
            findNavController().navigate(actionToTodo)
        }

        // 일정 수정&삭제 버튼 클릭 이벤트
        todoAdapter?.onItemClickListener = { itemPos, iv, tv, edt ->
            showEditPopup(itemPos, iv, tv, edt)
        }

        // 일정 완료/미완료 버튼 클릭 이벤트
        todoAdapter?.onItemCompleteClickListener = { itemPos ->
            // 아이템 정보 가져오기
            val item = todoAdapter?.getTodoList(itemPos)

            // 일정 완료/미완료 API 연결
            completeTodo(item!!.todoIdx, !item.isComplete, itemPos)
        }

        // 소지품 클릭 이벤트
        stuffAdapter?.onItemClickListener = { position ->
            val currentTime = System.currentTimeMillis()
            val stuff = stuffAdapter?.getStuff(position)

            // 클릭 시간 업데이트
            val previousClickTime = stuff?.lastClickTime ?: 0
            stuff?.lastClickTime = currentTime

            // 소지품을 처음 클릭했다면
            if (stuff != null) {
                if (stuff.handler == null) {
                    clickedStuffList.add(stuff.stuffName) // 클릭한 소지품 이름 추가

                    Log.d("1초 초반 소지품 클릭 시간 및 상태 업데이트", position.toString())

                    // 클릭 시간 업데이트
                    stuffAdapter?.updateStuffClickTime(position, currentTime)

                    // 클릭 상태로 변경
                    stuffAdapter?.changeStuffState(position, true)
                }
            }

            if (stuff != null) {
                if ((currentTime - previousClickTime) <= 1000) {
                    // 1초 이내에 동일한 소지품을 재클릭했다면 색상을 원상복구
                    Log.d("1초 이내 재클릭", position.toString())
                    stuffAdapter?.changeStuffState(position, false)

                    // 기존 handler 취소
                    stuff.handler?.removeCallbacksAndMessages(null)
                    stuff.handler = null

                    // 클릭한 소지품 이름 삭제
                    clickedStuffList.remove(stuff.stuffName)

                    stuffAdapter?.notifyDataSetChanged()
                }
                else {
                    // 소지품 1개 삭제(챙김 완료) API 연결
                    Log.d("1초 후", position.toString())

                    // 1초 후 삭제
                    val handler = Handler(Looper.getMainLooper())
                    val runnable = Runnable {
                        if (clickedStuffList.isNotEmpty()) {
                            Log.d("1초 후 삭제 소지품 목록", clickedStuffList.toString())
                            Log.d("1초 후 삭제할 소지품 이름", clickedStuffList.first())
                            Log.d("1초 후 삭제할 소지품 정보", stuffAdapter?.getStuffToName(clickedStuffList.first()).toString())
                            deleteStuff(selectDate, clickedStuffList.first(), stuffAdapter?.getStuffToName(clickedStuffList.first()))
                        }
                    }
                    handler.postDelayed(runnable, 1000)
                    stuff.handler = handler

                    stuffAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    // 주간 캘린더 설정 함수
    private fun setWeeklyCalendar(weekDate: LocalDateTime) {
        // 날짜 포맷
        val fullDateFormate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormate = DateTimeFormatter.ofPattern("dd")

        // 오늘 날짜 가져오기
        val today: String = CalendarUtils.getTodayDateList()[0].toString()

        // 요일 배열
        val weekDayList: Array<String> = resources.getStringArray(R.array.calendar_week_array)

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
    }

    // 월간 캘린더 - 일정 상태 표시(API 연동시)
    private fun setMonthlyCalendar() {
        // 새로운 decorators 추가
        for (i in 0 until monthlyItemList.size) {
            // LocalDate값 파싱하기
            val day: CalendarDay = CalendarDay.from(monthlyItemList[i].date.year, monthlyItemList[i].date.monthValue, monthlyItemList[i].date.dayOfMonth)
            // 완료된 일정이라면
            if (monthlyItemList[i].isComplete) {
                val eventDecorator = EventDecorator(requireContext(), R.color.Main_500, day)
                binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
            }
            else {
                val eventDecorator = EventDecorator(requireContext(), R.color.Main_400, day)
                binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
            }
        }
    }

    // 오늘 할 일 태그 구현 함수
    @SuppressLint("InflateParams")
    private fun setTodoTag(tagList: ArrayList<Int>) {
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

            // 텍스트 설정
            when (tagList[i]) {
                1 -> tagView.text = "일상"
                2 -> tagView.text = "직장"
                3 -> tagView.text = "취미"
                4 -> tagView.text = "공부"
                5 -> tagView.text = "운동"
                6 -> tagView.text = "독서"
                7 -> tagView.text = "여행"
                else -> tagView.text = "쇼핑"
            }

            // 뷰에 동적 layout 띄우기
            binding.lLayoutHomeTodoTag.addView(layout)
        }
    }

    // 동적 할 일 입력 뷰 추가
    private fun addTodoView() {
        // 동적 할 일 추가 뷰 초기화
        binding.lLayoutHomeTodoAdd.removeAllViews()

        // 동적 할 일 추가 뷰 띄우기
        binding.lLayoutHomeTodoAdd.visibility = View.VISIBLE

        // 동적으로 EditText 추가하기
        val edtView = EditText(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 36)

        // 각 버튼 이미지
        val completeBtn = ContextCompat.getDrawable(ApplicationClass.applicationContext(), R.drawable.ic_todo_default_rbtn_27)
        val editBtn = ContextCompat.getDrawable(ApplicationClass.applicationContext(), R.drawable.ic_more_27)

        // 스타일 적용
        edtView.apply {
            setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Black_1C)))
            textSize = 14f
            maxLines = 1
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 최대 20자까지 작성 가능하도록 설정
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_DONE
            setBackgroundResource(R.drawable.all_radius_12)
            typeface = Typeface.defaultFromStyle(Typeface.NORMAL) // 기본값 사용
            setPadding(48, 36, 48, 36)
            setCompoundDrawablesWithIntrinsicBounds(completeBtn, null, editBtn, null)
            compoundDrawablePadding = 24
            requestFocus()
            layoutParams = params
        }
        binding.lLayoutHomeTodoAdd.addView(edtView) // 레이아웃에 EditText 추가

        // 딜레이를 주어 키보드 띄우기
        edtView.postDelayed({
            getShowKeyboard(edtView)
        }, 300)

        // 동적 생성한 EditText에 대한 입력 처리
        setAddTodoViewEvent(edtView)
    }

    // 동적 생성한 오늘 할 일 EditText에 대한 입력 처리
    private fun setAddTodoViewEvent(edtView: EditText) {
        // 키보드의 완료 버튼을 눌렀을 경우
        edtView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 일정에 값이 없는 경우 뷰에서 EditText 삭제
                    if (edtView.text.toString().isBlank()) {
                        binding.lLayoutHomeTodoAdd.removeView(edtView)
                        binding.lLayoutHomeTodoAdd.visibility = View.GONE
                    }
                    // 일정에 값이 있는 경우 일정 1개 추가 API 연결
                    else {
                        // 일정 수정 API 연결
                        modifyTodo(
                            TodoModifyModel(
                                todoListItem!!.date.toString(),
                                todoListItem!!.todoTag!!,
                                LocalTime.of(todoListItem!!.goOutTime!!.hour, todoListItem!!.goOutTime!!.minute).toString(),
                                todoListItem!!.remindTime!!,
                                null,
                                null,
                                arrayListOf(edtView.text.toString())
                            )
                        )
                    }

                    getHideKeyboard(binding.root)
                    return true
                }
                return false
            }
        })

        // 화면 바깥을 클릭한 경우
        binding.lLayoutHomeInside.setOnClickListener {
            // 일정에 값이 없는 경우 뷰에서 EditText 삭제
            if (edtView.text.toString().isBlank()) {
                binding.lLayoutHomeTodoAdd.removeView(edtView)
                binding.lLayoutHomeTodoAdd.visibility = View.GONE
            }
            // 일정에 값이 있는 경우 일정 1개 추가 API 연결
            else {
                // 일정 수정 API 연결
                modifyTodo(
                    TodoModifyModel(
                        todoListItem!!.date.toString(),
                        todoListItem!!.todoTag!!,
                        LocalTime.of(todoListItem!!.goOutTime!!.hour, todoListItem!!.goOutTime!!.minute).toString(),
                        todoListItem!!.remindTime!!,
                        null,
                        null,
                        arrayListOf(edtView.text.toString())
                    )
                )
            }

            getHideKeyboard(binding.root)
        }

        // EditText에 Focus가 되어 있다면
        /*edtView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 화면 바깥을 클릭한 경우
                binding.lLayoutHomeInside.setOnClickListener {
                    // 일정에 값이 없는 경우 뷰에서 EditText 삭제
                    if (edtView.text.toString().isBlank()) {
                        binding.lLayoutHomeTodoAdd.removeView(edtView)
                        binding.lLayoutHomeTodoAdd.visibility = View.GONE
                    }
                    // 일정에 값이 있는 경우 일정 1개 추가 API 연결
                    else {
                        // 일정 수정 API 연결
                        modifyTodo(
                            TodoModifyModel(
                                todoListItem!!.date.toString(),
                                todoListItem!!.todoTag!!,
                                LocalTime.of(todoListItem!!.goOutTime!!.hour, todoListItem!!.goOutTime!!.minute).toString(),
                                todoListItem!!.remindTime!!,
                                null,
                                null,
                                arrayListOf(edtView.text.toString())
                            )
                        )
                    }

                    getHideKeyboard(binding.root)
                }
            }
        }*/
    }

    // 수정 팝업 띄우기(홈 화면의 오늘 할 일에서 사용)
    private fun showEditPopup(itemPos: Int, iv: ImageView?, tv: TextView, edt: EditText) {
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
                maxLines = 1
                filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 최대 20자까지 작성 가능하도록 설정

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
            binding.lLayoutHomeInside.setOnSingleClickListener {
                getEditDone(edt, tv, itemPos)
            }
        }

        // 삭제 버튼 클릭 이벤트
        val deleteBtn: LinearLayout = popup.findViewById(R.id.lLayout_home_todo_delete_popup)
        deleteBtn.setOnSingleClickListener {
            popupWindow.dismiss()

            // 일정 1개 삭제 API 연결
            deleteTodo(todoAdapter?.getTodoList(itemPos)!!.todoIdx, itemPos)

            /*// 일정이 1개라면
            if (todoAdapter?.itemCount == 1) {
                deleteAllTodo(TodoDeleteAllModel(selectDate.toString()))
            }
            // 일정이 여러 개라면
            else {
                // 일정 1개 삭제 API 연결
                deleteTodo(todoAdapter?.getTodoList(itemPos)!!.todoIdx, itemPos)
            }*/
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
            // 입력된 값이 기존 값과 다르다면
            if (edt.text.toString() != tv.text.toString()) {
                // 일정 1개 이름 수정 API 연결
                modifyTodoName(todoAdapter?.getTodoList(itemPos)!!.todoIdx, edt.text.toString(), TodoModifyItem(itemPos, edt, tv))
            }
        }
    }

    /* API 연동 함수 */
    // 일정 1개 조회
    private fun getTodo(todoDate: LocalDate) {
        val todoService = TodoService()
        todoService.setTodoView(this)
        todoService.getTodo(todoDate)
    }

    // 일정 1개 조회 성공
    override fun onGetTodoSuccess(
        status: Int,
        message: String,
        data: JsonObject?,
        todoDate: LocalDate?
    ) {
        when (status) {
            200 -> {
                // 동적 할 일 추가 뷰 숨기기
                binding.lLayoutHomeTodoAdd.visibility = View.GONE

                // rv 초기화
                todoAdapter?.resetTodoList()
                stuffAdapter?.resetStuffList()
                binding.lLayoutHomeTodoTag.removeAllViews()
                todoListItem = null

                // 일정이 있다면
                if (data != null) {
                    try {
                        // json data 파싱하기
                        val jsonObject = JSONObject(data.toString())

                        // inventoryList data
                        inventoryList.clear()
                        val tempInventoryList: String? = jsonObject.getString("inventoryList")
                        val inventoryJsonArray = JSONArray(tempInventoryList)
                        for (i in 0 until inventoryJsonArray.length()) {
                            val subJsonObject = inventoryJsonArray.getJSONObject(i)
                            val inventoryId = subJsonObject.getLong("inventoryId")
                            val inventoryName = subJsonObject.getString("inventoryName")
                            val inventoryIconIdx = subJsonObject.getInt("inventoryIconIdx")
                            val isInventoryUsed = subJsonObject.getBoolean("isInventoryUsed")

                            // 전체 인벤토리 리스트에 정보 저장
                            inventoryList.add(HomeInventoryItem(inventoryId, inventoryName, inventoryIconIdx, isInventoryUsed))
                        }

                        // todoList data
                        val tempTodoList: String? = jsonObject.getString("todoList")
                        val todoJsonArray = JSONArray(tempTodoList)
                        for (i in 0 until todoJsonArray.length()) {
                            val subJsonObject = todoJsonArray.getJSONObject(i)
                            val todoIdx = subJsonObject.getLong("todoIdx")
                            val todoName = subJsonObject.getString("todoName")
                            val isComplete = subJsonObject.getBoolean("isComplete")

                            // 오늘 할 일 리스트에 정보 저장
                            todoAdapter?.addTodoList(TodoItem(todoIdx, todoName, isComplete))
                        }

                        // todoTagList data
                        binding.lLayoutHomeTodoTag.removeAllViews()
                        val tempTodoTagList: JSONArray? = jsonObject.getJSONArray("todoTagList")
                        val todoTagList = ArrayList<Int>() // 일정 태그 리스트
                        for (i in 0 until (tempTodoTagList?.length() ?: 0)) {

                            // 오늘 할 일 태그 리스트에 정보 저장
                            todoTagList.add(tempTodoTagList?.get(i) as Int)
                        }

                        // 뷰에 태그 리스트 띄우기
                        setTodoTag(todoTagList)

                        // goOutTime data
                        val tempGoOutTime: String? = jsonObject.getString("goOutTime")
                        val goOutTime = LocalTime.parse(tempGoOutTime.toString())

                        // remindTime data
                        val tempRemindTime: JSONArray = jsonObject.getJSONArray("remindTime")
                        val remindTime = ArrayList<Int>() // 알림 시간 리스트
                        for (i in 0 until tempRemindTime.length()) {

                            // 알림 시간 리스트에 정보 저장
                            remindTime.add(tempRemindTime[i] as Int)
                        }

                        // stuffList data
                        val tempStuffList: String? = jsonObject.getString("stuffList")
                        val tempStuffJsonArray = JSONArray(tempStuffList)
                        for (i in 0 until tempStuffJsonArray.length()) {
                            val subJsonObject = tempStuffJsonArray.getJSONObject(i)
                            val stuffImgUrl = subJsonObject.getString("stuffImgUrl")
                            val stuffName = subJsonObject.getString("stuffName")

                            // 소지품 어댑터에 소지품 데이터 저장
                            stuffAdapter?.addStuffList(StuffItem(stuffImgUrl, stuffName, todoDate.toString(), false))
                        }

                        // 소지품 여부
                        val isCompleteStuff: Boolean = jsonObject.getBoolean("isCompleteStuff")

                        /* 데이터를 바탕으로 뷰 그리기 */
                        // 소지품을 다 챙겼다면
                        if (isCompleteStuff) {
                            // 할 일이 없다면
                            if (todoAdapter?.itemCount == 0) {
                                binding.tvHomeStuffDefault.visibility = View.GONE
                                binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                                binding.lLayoutHomeStuffComplete.visibility = View.VISIBLE
                                binding.iBtnHomeTodoAdd.visibility = View.GONE // 하단의 +버튼 숨기기
                                binding.ivHomeEdit.visibility = View.VISIBLE // 수정 버튼 띄우기
                            }
                            // 할 일이 있다면
                            else {
                                binding.tvHomeStuffDefault.visibility = View.GONE
                                binding.lLayoutHomeTodoDefault.visibility = View.GONE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                                binding.lLayoutHomeStuffComplete.visibility = View.VISIBLE
                                binding.iBtnHomeTodoAdd.visibility = View.VISIBLE // 하단의 +버튼 띄우기
                                binding.ivHomeEdit.visibility = View.VISIBLE // 수정 버튼 띄우기
                            }
                        }
                        // 소지품을 아직 안 챙겼다면
                        else {
                            // 소지품, 할 일이 없다면
                            if (stuffAdapter?.itemCount == 0 && todoAdapter?.itemCount == 0) {
                                binding.tvHomeStuffDefault.visibility = View.VISIBLE
                                binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                                binding.lLayoutHomeStuffComplete.visibility = View.GONE
                                binding.iBtnHomeTodoAdd.visibility = View.GONE
                                binding.ivHomeEdit.visibility = View.INVISIBLE // 수정 버튼 숨기기
                            }
                            // 소지품이 없지만, 할 일이 있다면
                            else if (stuffAdapter?.itemCount == 0 && todoAdapter?.itemCount!! >= 1) {
                                // 인벤토리가 없는 경우
                                binding.tvHomeStuffDefault.visibility = View.GONE
                                binding.lLayoutHomeTodoDefault.visibility = View.GONE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.VISIBLE
                                binding.lLayoutHomeStuffComplete.visibility = View.GONE
                                binding.iBtnHomeTodoAdd.visibility = View.VISIBLE // 하단의 +버튼 띄우기
                                binding.ivHomeEdit.visibility = View.INVISIBLE // 수정 버튼 숨기기
                            }
                            // 소지품은 있지만, 할 일은 없다면
                            else if (stuffAdapter?.itemCount!! >= 1 && todoAdapter?.itemCount == 0) {
                                binding.tvHomeStuffDefault.visibility = View.GONE
                                binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                                binding.lLayoutHomeStuffComplete.visibility = View.GONE
                                binding.iBtnHomeTodoAdd.visibility = View.GONE // 하단의 +버튼 숨기기
                                binding.ivHomeEdit.visibility = View.VISIBLE // 수정 버튼 띄우기
                            }
                            // 소지품도 있고, 할 일도 있다면
                            else if (stuffAdapter?.itemCount!! >= 1 && todoAdapter?.itemCount!! >= 1) {
                                binding.tvHomeStuffDefault.visibility = View.GONE
                                binding.lLayoutHomeTodoDefault.visibility = View.GONE
                                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                                binding.lLayoutHomeStuffComplete.visibility = View.GONE
                                binding.iBtnHomeTodoAdd.visibility = View.VISIBLE // 하단의 +버튼 띄우기
                                binding.ivHomeEdit.visibility = View.VISIBLE // 수정 버튼 띄우기
                            }
                        }

                        // 오늘 날짜의 일정 데이터 리스트 저장
                        todoListItem = TodoListItem(
                            todoItem = todoAdapter!!.getAllTodoList(),
                            date = todoDate,
                            todoTag = todoTagList,
                            goOutTime = goOutTime,
                            remindTime = remindTime
                        )

                    } catch (e: JSONException) {
                        Log.w("HomeFragment - Get Todo", e.stackTraceToString())
                        showToast(resources.getString(R.string.toast_server_error)) // 실패
                    }
                }
                // 일정이 없다면
                else {
                    // default 뷰 띄우기
                    binding.tvHomeStuffDefault.visibility = View.VISIBLE
                    binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                    binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                    binding.lLayoutHomeStuffComplete.visibility = View.GONE

                    // edit버튼, +버튼 숨기기
                    binding.ivHomeEdit.visibility = View.INVISIBLE
                    binding.iBtnHomeTodoAdd.visibility = View.GONE
                }
            }
        }
    }

    // 일정 1개 조회 실패
    override fun onGetTodoFailure(status: Int, message: String) {
        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401, 404 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }

    // 일정 전체(1달) 조회
    private fun getMonthlyTodo(date: LocalDate) {
        val todoService = TodoService()
        todoService.setTodoMonthlyView(this)
        todoService.getMonthlyTodo(date)
    }

    // 일정 전체(1달) 조회 성공
    override fun onGetMonthlyTodoSuccess(
        status: Int,
        message: String,
        data: JsonArray?
    ) {
        try {
            // 한달 데이터 리스트 초기화
            monthlyItemList.clear()

            // json data 파싱하기
            val jsonArray = JSONArray(data.toString())

            // date, isComplete data
            for (i in 0 until jsonArray.length()) {
                val subObject = jsonArray.getJSONObject(i)
                val dateObject = subObject.getString("date")
                val isCompleteObject = subObject.getBoolean("isComplete")

                // date -> LocalDate
                val date = LocalDate.parse(dateObject)

                // 한달 데이터 리스트 저장
                monthlyItemList.add(MonthlyItem(date, isCompleteObject))

                // 월간 캘린더 dot 추가
                setMonthlyCalendar()
            }
        } catch (e: JSONException) {
            Log.w("HomeFragment - Get Monthly Todo", e.stackTraceToString())
            showToast(resources.getString(R.string.toast_server_error)) // 실패
        }
    }

    // 일정 전체(1달) 조회 실패
    override fun onGetMonthlyTodoFailure(status: Int, message: String) {
        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401, 404 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }

    // 일정 수정
    private fun modifyTodo(todoModifyItem: TodoModifyModel) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.modifyTodo(todoModifyItem)
    }

    // 일정 1개 이름 수정
    private fun modifyTodoName(todoIdx: Long, todoName: String, todoModifyItem: TodoModifyItem) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.modifyTodoName(todoIdx, todoName, todoModifyItem)
    }

    // 일정 1개 삭제
    private fun deleteTodo(todoIdx: Long, itemPos: Int) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.deleteTodo(todoIdx, itemPos)
    }

    // 일정 전체 삭제
    private fun deleteAllTodo(date: TodoDeleteAllModel) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.deleteAllTodo(date)
    }

    // 일정 완료/미완료 변경
    private fun completeTodo(todoIdx: Long, isTodoComplete: Boolean, itemPos: Int) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.completeTodo(todoIdx, isTodoComplete, itemPos)
    }

    // 소지품 1개 삭제(소지품 챙기기 완료)
    private fun deleteStuff(date: LocalDate, stuffName: String, stuff: StuffItem?) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.deleteStuff(StuffDeleteModel(date.toString(), stuffName), stuff)
    }

    // 일정 1개 이름 수정/삭제 & 일정 전체 삭제 & 소지품 삭제 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (message) {
            "Todo Modify" -> {
                // 일정 1개 수정 성공
                val item = data as TodoModifyItem
                item.tv.text = item.edt.text.toString()
                todoAdapter?.modifyTodoList(item.itemPos, item.edt.text.toString()) // 입력한 값 저장 및 뷰의 아이템 값 수정
            }
            "Modify Todo" -> {
                // 일정 수정 성공
                // 일정 1개 조회 다시 불러오기
                binding.lLayoutHomeTodoAdd.removeAllViews()
                getTodo(selectDate)
            }
            "Todo Delete" -> {
                // 일정을 전부 삭제했다면
                if (data == null) {
                    todoAdapter?.resetTodoList()
                    stuffAdapter?.resetStuffList()
                    todoListItem = null
                    binding.rvHomeTodo.removeAllViews()
                    binding.rvHomeStuff.removeAllViews()
                    binding.lLayoutHomeTodoTag.removeAllViews()

                    // default 뷰 띄우기
                    binding.tvHomeStuffDefault.visibility = View.VISIBLE
                    binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                    binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                    binding.lLayoutHomeStuffComplete.visibility = View.GONE
                    binding.iBtnHomeTodoAdd.visibility = View.GONE
                    binding.ivHomeEdit.visibility = View.INVISIBLE // 수정 버튼 숨기기
                }
                // 일정 1개만 삭제했다면
                else {
                    // 아이템 삭제
                    val todoItem: TodoItem? = todoAdapter?.getTodoList(data as Int)
                    todoAdapter?.deleteTodoList(todoItem)
                }
            }
            "Todo Complete" -> {
                // 일정 완료/미완료 수정 성공
                val item = data as TodoCreateItem
                todoAdapter?.modifyTodoComplete(item.itemPos, item.isTodoComplete) // 입력한 값 저장 및 뷰의 아이템 값 수정

                // 월간 캘린더가 보인다면
                if (binding.mcvHomeMonthlyCalendarview.visibility == View.VISIBLE) {
                    // 캘린더 점 색상 변경
                    var isAllComplete = true
                    for (i in 0 until todoAdapter?.getAllTodoList()!!.size) {
                        // 일정 중 1개라도 미완료라면
                        if (!todoAdapter?.getTodoList(i)!!.isComplete) {
                            isAllComplete = false
                            break
                        }
                    }
                    // 일정이 모두 완료되었다면
                    if (isAllComplete) {
                        // LocalDate값 파싱하기
                        val day: CalendarDay = CalendarDay.from(selectDate.year, selectDate.monthValue, selectDate.dayOfMonth)
                        val eventDecorator = EventDecorator(requireContext(), R.color.Main_500, day)
                        binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
                    }
                    // 일정이 안 완료되었다면
                    else {
                        // LocalDate값 파싱하기
                        val day: CalendarDay = CalendarDay.from(selectDate.year, selectDate.monthValue, selectDate.dayOfMonth)
                        val eventDecorator = EventDecorator(requireContext(), R.color.Main_400, day)
                        binding.mcvHomeMonthlyCalendarview.addDecorator(eventDecorator)
                    }
                }
            }
            "Stuff Delete" -> {
                // 소지품 1개 삭제 성공
                val stuffItem = data as StuffItem
                stuffAdapter?.deleteStuff(stuffItem)
                clickedStuffList.remove(stuffItem.stuffName)

                // 소지품을 다 챙겼다면, 뷰 띄우기
                if (stuffAdapter?.itemCount == 0) {
                    binding.lLayoutHomeStuffComplete.visibility = View.VISIBLE
                }
            }
            /*"Delete All Todo" -> {
                // 리스트 초기화
                todoAdapter?.resetTodoList()
                stuffAdapter?.resetStuffList()
                todoListItem = null

                // default 뷰 띄우기
                binding.tvHomeStuffDefault.visibility = View.VISIBLE
                binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                binding.lLayoutHomeStuffComplete.visibility = View.GONE
                binding.viewHome.visibility = View.VISIBLE
                binding.lLayoutHomeTodoTag.removeAllViews()

                // edit버튼, +버튼 숨기기
                binding.ivHomeEdit.visibility = View.INVISIBLE
                binding.iBtnHomeTodoAdd.visibility = View.GONE

                // 월간 캘린더가 보인다면
                if (binding.mcvHomeMonthlyCalendarview.visibility == View.VISIBLE) {
                    // LocalDate값 파싱하기
                    val day: CalendarDay = CalendarDay.from(selectDate.year, selectDate.monthValue, selectDate.dayOfMonth)
                    val eventDecorator = EventDecorator(requireContext(), R.color.Main_400, day)
                    val eventDecorator2 = EventDecorator(requireContext(), R.color.Main_500, day)

                    // decorator 삭제하기
                    binding.mcvHomeMonthlyCalendarview.removeDecorator(eventDecorator)
                    binding.mcvHomeMonthlyCalendarview.removeDecorator(eventDecorator2)
                    binding.mcvHomeMonthlyCalendarview.invalidateDecorators()
                }
            }*/
        }
    }

    // 일정 1개 이름 수정/삭제 & 소지품 삭제 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401, 404 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }
}
