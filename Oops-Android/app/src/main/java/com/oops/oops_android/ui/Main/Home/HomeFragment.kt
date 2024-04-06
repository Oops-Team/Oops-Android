package com.oops.oops_android.ui.Main.Home

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
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
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Todo.Api.TodoMonthlyView
import com.oops.oops_android.data.remote.Todo.Api.TodoService
import com.oops.oops_android.data.remote.Todo.Api.TodoView
import com.oops.oops_android.data.remote.Todo.Model.StuffDeleteModel
import com.oops.oops_android.databinding.FragmentHomeBinding
import com.oops.oops_android.utils.CalendarUtils.Companion.getTodayDate
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

        // 일주일의 시작 날짜 가져오기
        val weekDate = CalendarUtils.getTodayDateList()[1] as LocalDateTime

        // 주간 캘린더 설정
        setWeeklyCalendar(weekDate)

        // 월간 캘린더
        binding.mcvHomeMonthlyCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기
    }

    override fun initAfterBinding() {

        // 일정 어댑터 연결
        todoAdapter = TodoListAdapter(requireContext())
        binding.rvHomeTodo.adapter = todoAdapter

        // 챙겨야 할 것 리스트 어댑터 연결
        stuffAdapter = StuffListAdapter(requireContext())
        binding.rvHomeStuff.adapter = stuffAdapter

        // 선택되어 있는 날짜 설정
        selectDate = LocalDate.parse(getTodayDate().toString().split("T")[0])

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
            val actionToTodo: NavDirections = HomeFragmentDirections.actionHomeFrmToTodoFrm(selectDate.toString(), todoListItem)
            findNavController().navigate(actionToTodo)
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
            // 소지품 1개 삭제(챙김 완료) API 연결
            deleteStuff(selectDate, stuffAdapter?.getStuffName(position).toString(), stuffAdapter?.getStuff(position))
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
            binding.lLayoutHomeInside.setOnClickListener {
                getEditDone(edt, tv, itemPos)
            }
        }

        // 삭제 버튼 클릭 이벤트
        val deleteBtn: LinearLayout = popup.findViewById(R.id.lLayout_home_todo_delete_popup)
        deleteBtn.setOnClickListener {
            popupWindow.dismiss()

            // 일정 1개 삭제 API 연결
            deleteTodo(todoAdapter?.getTodoList(itemPos)!!.todoIdx, itemPos)
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
                            stuffAdapter?.addStuffList(StuffItem(stuffImgUrl, stuffName, todoDate.toString()))
                        }

                        // 소지품 여부
                        val isCompleteStuff: Boolean = jsonObject.getBoolean("isCompleteStuff")

                        // 소지품을 다 챙겼을 경우
                        if (isCompleteStuff) {
                            binding.lLayoutHomeStuffComplete.visibility = View.VISIBLE
                        }

                        /* 데이터를 바탕으로 뷰 그리기 */
                        // 소지품이 1개 이상 있다면
                        if (stuffAdapter?.itemCount!! >= 1) {
                            binding.tvHomeStuffDefault.visibility = View.GONE
                            binding.lLayoutHomeTodoDefault.visibility = View.GONE
                            binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                            binding.lLayoutHomeStuffComplete.visibility = View.GONE
                        }

                        // 오늘 할 일이 1개 이상이지만, 소지품이 없다면
                        if (todoAdapter?.itemCount!! >= 1 && stuffAdapter?.itemCount == 0 && !isCompleteStuff) {
                            binding.tvHomeStuffDefault.visibility = View.GONE
                            binding.lLayoutHomeStuffNoInventory.visibility = View.VISIBLE
                            binding.lLayoutHomeTodoDefault.visibility = View.GONE
                            binding.lLayoutHomeStuffComplete.visibility = View.GONE
                            binding.iBtnHomeTodoAdd.visibility = View.VISIBLE // 하단의 +버튼 띄우기
                        }
                        // 오늘 할 일이 1개 이상 있다면
                        else if (todoAdapter?.itemCount!! >= 1) {
                            binding.lLayoutHomeTodoDefault.visibility = View.GONE // default 뷰 숨기기
                            binding.tvHomeStuffDefault.visibility = View.GONE
                            binding.lLayoutHomeStuffNoInventory.visibility = View.GONE
                            binding.iBtnHomeTodoAdd.visibility = View.VISIBLE // 하단의 +버튼 띄우기
                            binding.ivHomeEdit.visibility = View.VISIBLE // 수정 버튼 띄우기
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
                    binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                    binding.tvHomeStuffDefault.visibility = View.VISIBLE
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
        showToast(resources.getString(R.string.toast_server_error))
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
        showToast(resources.getString(R.string.toast_server_error))
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

    // 일정 1개 이름 수정/삭제 & 소지품 삭제 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (message) {
            "Todo Modify" -> {
                // 일정 1개 수정 성공
                val item = data as TodoModifyItem
                item.tv.text = item.edt.text.toString()
                todoAdapter?.modifyTodoList(item.itemPos, item.edt.text.toString()) // 입력한 값 저장 및 뷰의 아이템 값 수정
            }
            "Todo Delete" -> {
                // 아이템 삭제
                val todoItem: TodoItem? = todoAdapter?.getTodoList(data as Int)
                todoAdapter?.deleteTodoList(todoItem)

                // 만약 아이템이 아예 없다면
                if (todoAdapter?.itemCount == 0) {
                    binding.lLayoutHomeTodoDefault.visibility = View.VISIBLE
                    binding.viewHome.visibility = View.VISIBLE
                    binding.iBtnHomeTodoAdd.visibility = View.GONE
                    binding.rvHomeStuff.visibility = View.GONE
                    binding.lLayoutHomeTodoTag.visibility = View.GONE
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

                // 소지품을 다 챙겼다면, 뷰 띄우기
                if (stuffAdapter?.itemCount == 0) {
                    binding.lLayoutHomeStuffComplete.visibility = View.VISIBLE
                }
            }
        }
    }

    // 일정 1개 이름 수정/삭제 & 소지품 삭제 실패
    override fun onCommonFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}
