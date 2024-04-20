package com.oops.oops_android.ui.Main.Home.Todo

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonObject
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.R
import com.oops.oops_android.custom.SelectedDecorator
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Todo.Api.TodoService
import com.oops.oops_android.data.remote.Todo.Api.TodoView
import com.oops.oops_android.data.remote.Todo.Model.TodoCreateModel
import com.oops.oops_android.data.remote.Todo.Model.TodoDeleteAllModel
import com.oops.oops_android.data.remote.Todo.Model.TodoModifyItem2
import com.oops.oops_android.data.remote.Todo.Model.TodoModifyModel
import com.oops.oops_android.databinding.FragmentTodoBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.ui.Main.Home.TodoCheckModifyItem
import com.oops.oops_android.ui.Main.Home.TodoItem
import com.oops.oops_android.ui.Main.Home.TodoListItem
import com.oops.oops_android.utils.AlarmUtils.setCancelAlarm
import com.oops.oops_android.utils.AlarmUtils.setAllAlarm
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.setOnSingleClickListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

// 일정 추가 & 수정 화면
class TodoFragment: BaseFragment<FragmentTodoBinding>(FragmentTodoBinding::inflate),
    CompoundButton.OnCheckedChangeListener,
    CommonView,
    TodoView {
    private var remindList = ArrayList<Int>() // 선택된 알림 시간 리스트
    private var todoList = ArrayList<TodoCheckModifyItem>() // 모든 일정 리스트(Default, 추가한 일정)
    private var tagList = ArrayList<Int>() // 선택된 태그 리스트

    private var isShowDeleteBtn = false // EditText의 삭제 버튼 보임 여부
    private var isCreateBtnEnable = false // 등록하기 버튼 활성화 여부
    private var isEditBtnEnable = false // 수정하기 버튼 활성화 여부

    private var isOverlapName = false // 일정 이름 리스트 중복 여부
    private var isOverlapTag = false // 일정 수정 태그 중복 여부
    private var isOverlapRemind = false // 외출 알림 시간 중복 여부

    private var todoListItem: TodoListItem? = null // 기존에 작성되어 있던 아이템
    private lateinit var selectDate: LocalDate // 현재 사용자가 선택 중인 날짜
    private var isEdit = false // 현재 화면이 일정 추가 or 일정 수정 중 어느 화면인지 구분해주는 변수

    private var deleteTodoIdx = ArrayList<Long>() // 사용자가 삭제한 일정 idx 리스트
    private var addTodoName = ArrayList<EditText>() // 사용자가 추가한 일정 리스트

    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        // 선택한 날짜에 저장되어 있는 데이터가 있다면 가져오기
        try {
            val args: TodoFragmentArgs by navArgs()
            // 선택된 날짜 업데이트
            selectDate = LocalDate.parse(args.todoDate)

            // 툴 바 제목 설정
            if (args.todoListItem != null) {
                todoListItem = args.todoListItem

                // 일정 수정
                setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 수정")
                binding.lLayoutTodoEdit.visibility = View.VISIBLE // 버튼 띄우기
                isEdit = true

                /* 뷰 그리기 */
                // 오늘 할 일
                // 일정 개수만큼 EditText 추가
                for (i in 0 until todoListItem!!.todoItem.size) {
                    val tempEdt = addEditText()
                    todoList.add(TodoCheckModifyItem(todoListItem!!.todoItem[i].todoIdx, tempEdt, todoListItem!!.todoItem[i].todoName))
                    todoList[i].edt.setText(todoListItem!!.todoItem[i].todoName) // 일정 edittext 리스트
                }
                addTodoName.clear()

                // 관련 태그
                for (i in 0 until todoListItem!!.todoTag!!.size) {
                    // 일상
                    if (todoListItem!!.todoTag!![i] == 1) {
                        binding.cbTagDaily.isChecked = true
                        binding.cbTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 직장
                    else if (todoListItem!!.todoTag!![i] == 2) {
                        binding.cbTagJob.isChecked = true
                        binding.cbTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 취미
                    else if (todoListItem!!.todoTag!![i] == 3) {
                        binding.cbTagHobby.isChecked = true
                        binding.cbTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 공부
                    else if (todoListItem!!.todoTag!![i] == 4) {
                        binding.cbTagStudy.isChecked = true
                        binding.cbTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 운동
                    else if (todoListItem!!.todoTag!![i] == 5) {
                        binding.cbTagSports.isChecked = true
                        binding.cbTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 독서
                    else if (todoListItem!!.todoTag!![i] == 6) {
                        binding.cbTagReading.isChecked = true
                        binding.cbTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 여행
                    else if (todoListItem!!.todoTag!![i] == 7) {
                        binding.cbTagTravel.isChecked = true
                        binding.cbTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 쇼핑
                    else if (todoListItem!!.todoTag!![i] == 8) {
                        binding.cbTagShopping.isChecked = true
                        binding.cbTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                }
                onCheckedChanged(binding.cbTagDaily, true)

                // 외출 시간
                binding.timepickerTodo.hour = todoListItem!!.goOutTime!!.hour
                binding.timepickerTodo.minute = todoListItem!!.goOutTime!!.minute

                // 알림 시간
                for (i in 0 until todoListItem!!.remindTime!!.size) {
                    // 없음
                    if (todoListItem!!.remindTime!![i] == 1) {
                        binding.cbRemindNo.isChecked = true
                        remindList.add(1)
                        binding.cbRemindNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 5분전
                    else if (todoListItem!!.remindTime!![i] == 2) {
                        binding.cbRemind5m.isChecked = true
                        remindList.add(2)
                        binding.cbRemind5m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 30분전
                    else if (todoListItem!!.remindTime!![i] == 3) {
                        binding.cbRemind30m.isChecked = true
                        remindList.add(3)
                        binding.cbRemind30m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 1시간전
                    else if (todoListItem!!.remindTime!![i] == 4) {
                        binding.cbRemind1h.isChecked = true
                        remindList.add(4)
                        binding.cbRemind1h.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                    // 하루전
                    else if (todoListItem!!.remindTime!![i] == 5) {
                        binding.cbRemind1d.isChecked = true
                        remindList.add(5)
                        binding.cbRemind1d.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    }
                }
            }
            else {
                // 일정 추가
                setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 추가")
                binding.btnTodoCreate.visibility = View.VISIBLE
                val tempEdt = addEditText()
                todoList.add(TodoCheckModifyItem(null, tempEdt, null))
                isEdit = false
            }
        } catch (e: Exception) {
            Log.d("TodoFragment - Get Nav Data", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 월간 캘린더
        binding.mcvTodoCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기

        /* 캘린더에 선택 날짜 띄우기 */
        val dateFormatList = selectDate.toString().split("-") // 정보 분리
        binding.mcvTodoCalendarview.selectedDate = CalendarDay.from(dateFormatList[0].toInt(), dateFormatList[1].toInt(), dateFormatList[2].toInt())
        val decorator = SelectedDecorator(requireContext(), binding.mcvTodoCalendarview.selectedDate)
        binding.mcvTodoCalendarview.addDecorator(decorator) // 월간 캘린더에 dot 적용
        binding.mcvTodoCalendarview.currentDate = binding.mcvTodoCalendarview.selectedDate // 월간 캘린더에 선택 날짜 적용

        // 뒤로 가기 버튼 클릭
        binding.toolbarTodo.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 휴지통 버튼을 누른 경우
        binding.ivTodoDelete.setOnClickListener {
            getHideKeyboard(binding.root) // 키보드 숨기기
            clickDeleteBtn() // delete 버튼 숨기기 or 보여주기
        }

        // 캘린더 날짜 버튼을 누른 경우
        binding.mcvTodoCalendarview.setOnDateChangedListener { widget, date, selected ->
            val tempSelectedDate = LocalDate.of(date.year, date.month, date.day) // 선택한 날짜
            val defaultDate = CalendarDay.from(selectDate.year, selectDate.monthValue, selectDate.dayOfMonth) // 기존에 선택된 날짜

            // 일정 추가의 경우
            if (!isEdit) {
                // 값이 입력되어 있다면
                if (todoList[0].edt.text.toString().isNotBlank()) {
                    // 값 선택 해제 및 날짜 값 원상 복구
                    val eventDecorator = SelectedDecorator(requireContext(), defaultDate)
                    binding.mcvTodoCalendarview.addDecorator(eventDecorator)
                    binding.mcvTodoCalendarview.selectedDate = defaultDate

                    showCustomSnackBar(resources.getString(R.string.snackbar_todo_change))
                }
                // 값이 안 입력되어 있다면
                else {
                    // 선택 날짜로 캘린더 변경
                    val eventDecorator = SelectedDecorator(requireContext(), date)
                    binding.mcvTodoCalendarview.addDecorator(eventDecorator)
                    selectDate = tempSelectedDate

                    // 일정 1개 조회 API 연결
                    getTodo(selectDate)
                }
            }
            // 일정 수정의 경우
            else {
                // 값이 변경되었다면(일정 수정하기 버튼이 활성화되어 있다면)
                if (isEditBtnEnable) {
                    // 값 선택 해제 및 날짜 값 원상 복구
                    val eventDecorator = SelectedDecorator(requireContext(), defaultDate)
                    binding.mcvTodoCalendarview.addDecorator(eventDecorator)
                    binding.mcvTodoCalendarview.selectedDate = defaultDate

                    showCustomSnackBar(resources.getString(R.string.snackbar_todo_change))
                }
                // 값이 안 입력되어 있다면
                else {
                    // 선택 날짜로 캘린더 변경
                    val eventDecorator = SelectedDecorator(requireContext(), date)
                    binding.mcvTodoCalendarview.addDecorator(eventDecorator)
                    selectDate = tempSelectedDate

                    // 일정 1개 조회 API 연결
                    getTodo(selectDate)
                }
            }
        }

        // 오늘 할 일 추가 버튼을 누른 경우
        binding.iBtnTodo.setOnClickListener {
            // 30개 이상이라면
            if (todoList.size >= 30) {
                showCustomSnackBar(getString(R.string.toast_todo_not_add)) // 스낵바 띄우기
            }
            else {
                // - 버튼이 보이는 상태라면
                if (isShowDeleteBtn) {
                    // - 버튼 모두 숨기기
                    for (i in 0 until todoList.size) {
                        // 각 EditText의 delete 버튼이 보인다면
                        todoList[i].edt.setCompoundDrawables(null, null, null, null) // delete 버튼 숨기기
                        isShowDeleteBtn = false
                    }
                }
                // 동적 EditText 추가하기
                val tempEdt = addEditText()
                todoList.add(TodoCheckModifyItem(null, tempEdt, null)) // 리스트에 EditText 추가
            }
        }

        // 각 태그 선택한 경우에 따른 이벤트 처리
        setOnCheckedChanged(binding.cbTagDaily)
        setOnCheckedChanged(binding.cbTagJob)
        setOnCheckedChanged(binding.cbTagHobby)
        setOnCheckedChanged(binding.cbTagStudy)
        setOnCheckedChanged(binding.cbTagSports)
        setOnCheckedChanged(binding.cbTagReading)
        setOnCheckedChanged(binding.cbTagTravel)
        setOnCheckedChanged(binding.cbTagShopping)

        // 알림 시간 없음을 클릭한 경우
        binding.cbRemindNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                remindList.clear() // 배열 항목 모두 삭제
                remindList.add(1)
                binding.cbRemindNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))

                // 선택되어 있던 항목 체크 해제
                binding.cbRemind5m.isChecked = false
                binding.cbRemind30m.isChecked = false
                binding.cbRemind1h.isChecked = false
                binding.cbRemind1d.isChecked = false
                binding.cbRemind5m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
                binding.cbRemind30m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
                binding.cbRemind1h.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
                binding.cbRemind1d.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))

                // 등록하기 버튼 활성화 여부 체크
                updateBtnUI()
            }
        }

        // 5분 전을 선택한 경우
        binding.cbRemind5m.setOnCheckedChangeListener { checkBox, isChecked ->
            setRemindCheckedChanged(checkBox, isChecked, 2)
        }

        // 30분 전을 선택한 경우
        binding.cbRemind30m.setOnCheckedChangeListener { checkBox, isChecked ->
            setRemindCheckedChanged(checkBox, isChecked, 3)
        }

        // 1시간 전을 선택한 경우
        binding.cbRemind1h.setOnCheckedChangeListener { checkBox, isChecked ->
            setRemindCheckedChanged(checkBox, isChecked, 4)
        }

        // 하루 전을 선택한 경우
        binding.cbRemind1d.setOnCheckedChangeListener { checkBox, isChecked ->
            setRemindCheckedChanged(checkBox, isChecked, 5)
        }

        // 외출 시간을 변경한 경우
        binding.timepickerTodo.setOnTimeChangedListener { _, _, _ ->
            updateBtnUI()
        }

        // 등록하기 버튼을 클릭한 경우
        binding.btnTodoCreate.setOnSingleClickListener {
            // 버튼 활성화가 되어 있다면
            if (isCreateBtnEnable) {
                // 일정 목록 불러오기
                val tempList = ArrayList<String>()
                for (i in 0 until todoList.size) {
                    if (todoList[i].edt.text.toString().isNotBlank()) {
                        tempList.add(todoList[i].edt.text.toString())
                    }
                }

                // 일정 추가 API 연결
                createTodo(
                    TodoCreateModel(
                        date = selectDate.toString(),
                        todoName = tempList,
                        todoTag = tagList,
                        goOutTime = LocalTime.of(binding.timepickerTodo.hour, binding.timepickerTodo.minute).toString(),
                        remindTime = remindList
                    )
                )
            }
        }

        // 수정하기 버튼을 클릭한 경우
        binding.btnTodoEdit.setOnSingleClickListener {
            // 버튼 활성화가 되어 있다면
            if (isEditBtnEnable) {
                // 새롭게 추가한 일정 리스트
                val tempTodoAddList = ArrayList<String>()
                for (i in 0 until addTodoName.size) {
                    if (addTodoName[i].text.toString().isNotBlank()) {
                        tempTodoAddList.add(addTodoName[i].text.toString())
                    }
                }

                // 수정한 일정 리스트
                val tempTodoModifyList = ArrayList<TodoModifyItem2>()
                for (i in 0 until todoList.size) {
                    // 이름이 수정되었다면
                    if (todoList[i].todoName != null) {
                        if (todoList[i].edt.text.toString() != todoList[i].todoName) {
                            tempTodoModifyList.add(TodoModifyItem2(todoList[i].id!!, todoList[i].edt.text.toString()))
                        }
                    }
                }

                // 일정 수정 API 연결
                modifyTodo(
                    TodoModifyModel(
                        date = selectDate.toString(),
                        todoTag = tagList,
                        goOutTime = LocalTime.of(binding.timepickerTodo.hour, binding.timepickerTodo.minute).toString(),
                        remindTime = remindList,
                        deleteTodoIdx = deleteTodoIdx,
                        modifyTodo = tempTodoModifyList,
                        addTodoName = tempTodoAddList
                    )
                )
            }
        }

        // 삭제하기 버튼을 클릭한 경우
        binding.btnTodoDelete.setOnSingleClickListener {
            // 일정 삭제 팝업 띄우기
            val todoDeleteDialog = TodoDeleteDialog(requireContext())
            todoDeleteDialog.showTodoDeleteDialog()

            // 예 버튼을 누른 경우
            todoDeleteDialog.setOnClickedListener(object : TodoDeleteDialog.TodoDeleteBtnClickListener {
                override fun onClicked() {
                    // 일정 전체 삭제 API 연동
                    deleteAllTodo(TodoDeleteAllModel(selectDate.toString()))
                }
            })
        }
    }

    // 동적 EditText 추가하기
    private fun addEditText(): EditText {
        // 동적으로 EditText 추가하기
        val edtView = EditText(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            102,
        )
        params.setMargins(0, 24, 0, 0)
        // 스타일 적용
        edtView.apply {
            hint = getString(R.string.todo_today_info)
            setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_300)))
            setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_900)))
            textSize = 14f
            maxLines = 1
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 최대 20자까지 작성 가능하도록 설정
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_DONE
            setBackgroundResource(R.drawable.edit_text_bg)
            typeface = Typeface.defaultFromStyle(Typeface.NORMAL) // 기본값 사용
            setPadding(0, 16, 0, 8)
            layoutParams = params
        }
        binding.lLayoutTodo.addView(edtView) // 레이아웃에 EditText 추가
        addTodoName.add(edtView) // 추가된 일정 리스트에 추가(일정 수정 시 전달 예정)

        // 동적 생성한 EditText에 대한 입력 처리
        setOtherEdtEvent(edtView)

        return edtView
    }

    // 동적 생성한 EditText에 대한 입력 처리
    @SuppressLint("ClickableViewAccessibility")
    private fun setOtherEdtEvent(edtView: EditText) {
        // 완료 버튼을 눌렀을 경우
        edtView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 일정에 값이 없는 경우 EditText 삭제
                    if (edtView.text.toString().isBlank() && todoList.size > 1) {
                        for (index in 0 until todoList.size) {
                            if (todoList[index].edt == edtView) {

                                if (todoList[index].id != null) {
                                    deleteTodoIdx.add(todoList[index].id!!) // 삭제한 일정 id를 리스트에 추가
                                }
                                todoList.removeAt(index) // 전체 리스트에서 값 삭제
                                binding.lLayoutTodo.removeView(edtView)
                                break
                            }
                        }
                        for (index2 in 0 until addTodoName.size) {
                            if (addTodoName[index2] == edtView) {
                                addTodoName.removeAt(index2)
                                break
                            }
                        }

                        // 등록하기 버튼 활성화 여부 체크
                        updateBtnUI()
                    }

                    // 일정 값이 수정된 경우
                    getHideKeyboard(binding.root)
                    return true
                }
                return false
            }
        })

        // EditText의 값이 바뀌고 있다면
        edtView.onTextChanged {
            updateBtnUI()
        }

        // EditText에 Focus가 되어 있다면
        edtView.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 화면 바깥을 클릭한 경우
                binding.lLayoutTodoTop.setOnClickListener {
                    // 일정에 값이 없는 경우 EditText 삭제
                    if (edtView.text.toString().isBlank() && todoList.size > 1) {
                        for (index in 0 until todoList.size) {
                            if (todoList[index].edt == edtView) {

                                if (todoList[index].id != null) {
                                    deleteTodoIdx.add(todoList[index].id!!) // 삭제한 일정 id를 리스트에 추가
                                }
                                todoList.removeAt(index) // 전체 리스트에서 값 삭제
                                binding.lLayoutTodo.removeView(edtView)
                                break
                            }
                        }
                        for (index2 in 0 until addTodoName.size) {
                            if (addTodoName[index2] == edtView) {
                                addTodoName.removeAt(index2)
                                break
                            }
                        }

                        // 등록하기 버튼 활성화 여부 체크
                        updateBtnUI()
                    }

                    getHideKeyboard(binding.root)
                }
            }
        }

        // 삭제 버튼을 눌렀다면
        edtView.setOnTouchListener(View.OnTouchListener { _, event ->
            getHideKeyboard(binding.root) // 키보드 숨기기

            val DRAWABLE_RIGHT = 2
            try {
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (edtView.right -
                                edtView.compoundDrawables[DRAWABLE_RIGHT].bounds.width() - 12)) {

                        for (index in 0 until todoList.size) {
                            if (todoList[index].edt == edtView) {

                                if (todoList[index].id != null) {
                                    deleteTodoIdx.add(todoList[index].id!!) // 삭제한 일정 id를 리스트에 추가
                                }
                                todoList.removeAt(index) // 전체 리스트에서 값 삭제
                                binding.lLayoutTodo.removeView(edtView)
                                break
                            }
                        }
                        for (index2 in 0 until addTodoName.size) {
                            if (addTodoName[index2] == edtView) {
                                addTodoName.removeAt(index2)
                                break
                            }
                        }

                        // 등록하기 버튼 활성화 여부 체크
                        updateBtnUI()

                        return@OnTouchListener true
                    }
                }
                false
            } catch (e: Exception) {
                false
            }
        })
    }

    // 휴지통 클릭 이벤트 처리 함수
    @SuppressLint("ClickableViewAccessibility")
    private fun clickDeleteBtn() {
        if (isShowDeleteBtn) {
            for (i in 0 until todoList.size) {
                // 각 EditText의 delete 버튼이 보인다면
                todoList[i].edt.setCompoundDrawables(null, null, null, null) // delete 버튼 숨기기
                isShowDeleteBtn = false
            }
        }
        // 각 EditText의 delete 버튼이 안 보인다면
        else {
            showTodoDeleteBtn()
        }
    }

    // 일정 삭제 버튼 이벤트
    @SuppressLint("ClickableViewAccessibility")
    private fun showTodoDeleteBtn() {
        isShowDeleteBtn = true
        // delete 버튼
        val deleteBtn = ContextCompat.getDrawable(requireContext(), R.drawable.ic_todo_delete_24)

        // 동적으로 생성된 EditText의 갯수
        val todoCount = binding.lLayoutTodo.childCount

        // 동적으로 생성된 EditText에 delete 버튼 띄우기
        for (i in 0 until todoCount) {
            val editText: EditText = binding.lLayoutTodo.getChildAt(i) as EditText
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, deleteBtn, null)
        }
    }

    // 알림 시간 선택 이벤트 처리 함수
    private fun setRemindCheckedChanged(checkBox: CompoundButton, isChecked: Boolean, itemCode: Int) {
        if (isChecked) { // 선택했다면
            remindList.add(itemCode) // 값 추가
            checkBox.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))

            // 없음 항목 체크 해제
            if (binding.cbRemindNo.isChecked) {
                remindList.remove(1)
                binding.cbRemindNo.isChecked = false
                binding.cbRemindNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
            }
        }
        else { // 선택을 해제했다면
            remindList.remove(itemCode) // 선택 해제한 값 삭제
            checkBox.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }

        // 등록하기 버튼 활성화 여부 체크
        updateBtnUI()
    }

    // 태그 선택 이벤트 연결 함수
    private fun setOnCheckedChanged(checkBox: CompoundButton) {
        checkBox.setOnCheckedChangeListener { view, isChecked ->
            onCheckedChanged(view, isChecked)

            // 등록하기 버튼 활성화 여부 체크
            updateBtnUI()
        }
    }

    // 태그 및 알림 시간 선택 이벤트
    override fun onCheckedChanged(checkBox: CompoundButton, isChecked: Boolean) {
        tagList.clear()
        var tagCount = 0 // 선택된 태그 갯수

        // 체크 박스를 선택 했다면 리스트에 데이터 추가
        if (binding.cbTagDaily.isChecked) {
            tagList.add(1)
            binding.cbTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagJob.isChecked) {
            tagList.add(2)
            binding.cbTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagHobby.isChecked) {
            tagList.add(3)
            binding.cbTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagStudy.isChecked) {
            tagList.add(4)
            binding.cbTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagSports.isChecked) {
            tagList.add(5)
            binding.cbTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagReading.isChecked) {
            tagList.add(6)
            binding.cbTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagTravel.isChecked) {
            tagList.add(7)
            binding.cbTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbTagShopping.isChecked) {
            tagList.add(8)
            binding.cbTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }

        // 선택된 데이터 구하기
        for (i in 0 until tagList.size) {
            ++tagCount // count 개수 세기

            // 선택된 데이터가 없다면
            if (tagList.isEmpty()) {
                tagCount = 0
            }
            // tagCount 개수가 3개 이상 이라면
            else if (tagCount >= 3) {
                // 선택되지 않은 체크박스를 비활성화 시키기
                if (!binding.cbTagDaily.isChecked) {
                    binding.cbTagDaily.isEnabled = false
                }
                if (!binding.cbTagJob.isChecked) {
                    binding.cbTagJob.isEnabled = false
                }
                if (!binding.cbTagHobby.isChecked) {
                    binding.cbTagHobby.isEnabled = false
                }
                if (!binding.cbTagStudy.isChecked) {
                    binding.cbTagStudy.isEnabled = false
                }
                if (!binding.cbTagSports.isChecked) {
                    binding.cbTagSports.isEnabled = false
                }
                if (!binding.cbTagReading.isChecked) {
                    binding.cbTagReading.isEnabled = false
                }
                if (!binding.cbTagTravel.isChecked) {
                    binding.cbTagTravel.isEnabled = false
                }
                if (!binding.cbTagShopping.isChecked) {
                    binding.cbTagShopping.isEnabled = false
                }
            }
            // tagCount 개수가 3개 미만 이라면
            else {
                // 모든 체크박스 활성화
                binding.cbTagDaily.isEnabled = true
                binding.cbTagJob.isEnabled = true
                binding.cbTagHobby.isEnabled = true
                binding.cbTagStudy.isEnabled = true
                binding.cbTagSports.isEnabled = true
                binding.cbTagReading.isEnabled = true
                binding.cbTagTravel.isEnabled = true
                binding.cbTagShopping.isEnabled = true
            }
        }
    }

    // 두 개의 리스트의 값이 같은지 확인하는 함수(관련 태그 리스트)
    private fun compareTagLists(defaultTagList: ArrayList<Int>?, tagList: ArrayList<Int>): Boolean {
        // 두 배열을 오름차순으로 재정렬
        val sortedDefaultTagList = defaultTagList?.sorted()
        val sortedTagList = tagList.sorted()

        // 두 리스트의 크기가 다르다면
        if (sortedDefaultTagList!!.size != sortedTagList.size) {
            return false
        }

        val result = sortedDefaultTagList.zip(sortedTagList)

        // 두 리스트의 값이 다르다면
        for ((item1, item2) in result) {
            if (item1 != item2) {
                return false
            }
        }

        // 위의 2가지 조건을 모두 통과했다면 값 같음
        return true
    }

    // 두 개의 리스트의 값이 같은지 확인하는 함수(일정 이름 리스트)
    private fun compareNameLists(defaultNameList: ArrayList<String>?, nameList: ArrayList<String>): Boolean {
        // 두 리스트의 크기가 다르다면
        if (defaultNameList!!.size != nameList.size) {
            return false
        }

        val result = defaultNameList.zip(nameList)

        // 두 리스트의 값이 다르다면
        for ((item1, item2) in result) {
            if (item1 != item2) {
                return false
            }
        }

        // 위의 2가지 조건을 모두 통과했다면 값 같음
        return true
    }

    // 등록하기 버튼 색상 변경
    private fun updateBtnUI() {
        // 일정 목록 불러오기
        val tempList = ArrayList<String>()
        for (i in 0 until todoList.size) {
            if (todoList[i].edt.text.toString().isNotBlank()) {
                tempList.add(todoList[i].edt.text.toString())
            }
        }

        // 필수값 입력이 안 되었다면
        if (tempList.isEmpty()) {
            // 버튼 활성화 해제
            binding.btnTodoCreate.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoCreate.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isCreateBtnEnable = false

            binding.btnTodoEdit.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isEditBtnEnable = false
        }
        // 태그 입력이 안 되어 있다면
        else if (tagList.isEmpty()) {
            // 버튼 활성화 해제
            binding.btnTodoCreate.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoCreate.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isCreateBtnEnable = false

            binding.btnTodoEdit.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isEditBtnEnable = false
        }
        // 알림 시간 입력이 안 되어 있다면
        else if (remindList.isEmpty()) {
            // 버튼 활성화 해제
            binding.btnTodoCreate.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoCreate.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isCreateBtnEnable = false

            binding.btnTodoEdit.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnTodoEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
            isEditBtnEnable = false
        }
        // 모두 입력되었다면
        else {
            // 일정 수정 화면이라면
            if (isEdit) {
                try {
                    /* 기존에 입력된 값과 같은지 확인 */
                    val tempNameList = ArrayList<String>()
                    for (i in 0 until todoList.size) {
                        // 일정 이름 리스트 만들기
                        tempNameList.add(todoList[i].todoName.toString()) // 기존 일정 이름 리스트
                    }
                    val tempNameList2 = ArrayList<String>()
                    for (i in 0 until todoList.size) {
                        tempNameList2.add(todoList[i].edt.text.toString()) // 기존 + 수정된 일정 이름 리스트
                    }
                    // 일정 이름 리스트 중복이 아니라면(= 값이 안 바뀌었다면)
                    isOverlapName = compareNameLists(tempNameList, tempNameList2)
                    if (!isOverlapName) {
                        // 버튼 활성화 상태가 아니라면
                        if (!isEditBtnEnable) {
                            ButtonUtils().setAllColorAnimation(binding.btnTodoEdit)
                            isEditBtnEnable = true
                        }
                    }

                    // 관련 태그 중복이 아니라면
                    // item의 태그와 선택한 태그가 중복이라면(= 값이 안 바뀌었다면)
                    isOverlapTag = compareTagLists(todoListItem!!.todoTag, tagList)
                    if (!isOverlapTag) {
                        // 버튼 활성화 상태가 아니라면
                        if (!isEditBtnEnable) {
                            ButtonUtils().setAllColorAnimation(binding.btnTodoEdit)
                            isEditBtnEnable = true
                        }
                    }

                    // 외출 시간 중복이 아니라면
                    if (todoListItem!!.goOutTime != LocalTime.of(binding.timepickerTodo.hour, binding.timepickerTodo.minute)) {
                        // 버튼 활성화 상태가 아니라면
                        if (!isEditBtnEnable) {
                            ButtonUtils().setAllColorAnimation(binding.btnTodoEdit)
                            isEditBtnEnable = true
                        }
                    }

                    // 외출 알림 시간 중복이 아니라면
                    isOverlapRemind = compareTagLists(todoListItem!!.remindTime, remindList)
                    if (!isOverlapRemind) {
                        // 버튼 활성화 상태가 아니라면
                        if (!isEditBtnEnable) {
                            ButtonUtils().setAllColorAnimation(binding.btnTodoEdit)
                            isEditBtnEnable = true
                        }
                    }

                    // 모든 값이 중복이라면
                    if (isOverlapName && isOverlapTag && isOverlapRemind &&
                        todoListItem!!.goOutTime == LocalTime.of(binding.timepickerTodo.hour, binding.timepickerTodo.minute)
                        ) {
                        binding.btnTodoEdit.setTextAppearance(R.style.WideButtonDisableStyle)
                        binding.btnTodoEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
                        isEditBtnEnable = false
                    }

                    // 삭제한 일정이 있다면
                    if (deleteTodoIdx.isNotEmpty()) {
                        // 버튼 활성화 상태가 아니라면
                        if (!isEditBtnEnable) {
                            ButtonUtils().setAllColorAnimation(binding.btnTodoEdit)
                            isEditBtnEnable = true
                        }
                    }

                } catch (e: Exception) {
                    Log.d("TodoFragment - Update Edit Btn UI", e.stackTraceToString())
                }
            }
            // 일정 추가 화면이라면
            else {
                // 버튼 활성화 상태가 아니라면
                if (!isCreateBtnEnable) {
                    ButtonUtils().setAllColorAnimation(binding.btnTodoCreate)
                    isCreateBtnEnable = true
                }
            }
        }
    }

    // 일정 추가
    private fun createTodo(todoItem: TodoCreateModel) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.createTodo(todoItem)
    }

    // 일정 수정
    private fun modifyTodo(todoModifyItem: TodoModifyModel) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.modifyTodo(todoModifyItem)
    }

    // 일정 삭제
    private fun deleteAllTodo(date: TodoDeleteAllModel) {
        val todoService = TodoService()
        todoService.setCommonView(this)
        todoService.deleteAllTodo(date)
    }

    // 일정 추가 성공 & 일정 수정 & 일정 전체 삭제 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (message) {
            // 일정 추가
            "Create Todo" -> {
                // 알람 등록하기
                setAllAlarm(requireContext(), selectDate, binding.timepickerTodo.hour, binding.timepickerTodo.minute, remindList)

                // 일정 추가 성공 팝업 띄우기
                val todoCreateDialog = TodoCreateDialog(requireContext())
                todoCreateDialog.showTodoCreateDialog()

                // 확인 버튼을 누른 경우
                todoCreateDialog.setOnClickedListener(object : TodoCreateDialog.TodoCreateBtnClickListener {
                    override fun onClicked() {
                        // 홈 화면으로 이동하기
                        val actionToHome: NavDirections = TodoFragmentDirections.actionTodoFrmToHomeFrm()
                        findNavController().navigate(actionToHome)
                    }
                })
            }

            // 일정 수정
            "Modify Todo" -> {
                // 알람 등록하기
                setAllAlarm(requireContext(), selectDate, binding.timepickerTodo.hour, binding.timepickerTodo.minute, remindList)

                // 일정 수정 성공 팝업 띄우기
                val todoModifyDialog = TodoModifyDialog(requireContext())
                todoModifyDialog.showTodoModifyDialog()

                // 확인 버튼을 누른 경우
                todoModifyDialog.setOnClickedListener(object : TodoModifyDialog.TodoModifyBtnClickListener {
                    override fun onClicked() {
                        // 홈 화면으로 이동하기
                        val actionToHome: NavDirections = TodoFragmentDirections.actionTodoFrmToHomeFrm()
                        findNavController().navigate(actionToHome)
                    }
                })
            }

            // 일정 삭제
            "Delete All Todo" -> {
                // 알림 삭제하기
                setCancelAlarm(requireContext(), selectDate)

                // 삭제 성공 팝업 띄우기
                val todoDeleteAgreeDialog = TodoDeleteAgreeDialog(requireContext())
                todoDeleteAgreeDialog.showTodoDeleteAgreeDialog()
                // 확인 버튼을 누른 경우
                todoDeleteAgreeDialog.setOnClickedListener(object : TodoDeleteAgreeDialog.TodoDeleteAgreeBtnClickListener{
                    override fun onClicked() {
                        // 홈 화면으로 이동
                        val actionToHome: NavDirections = TodoFragmentDirections.actionTodoFrmToHomeFrm()
                        findNavController().navigate(actionToHome)
                    }
                })
            }
        }
    }

    // 일정 추가 성공 & 일정 수정 & 일정 전체 삭제 실패
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
                // 값과 뷰 초기화
                todoListItem = null
                setClearView()

                // 일정이 있다면(일정 수정 화면이라면)
                if (data != null) {
                    try {
                        // 일정 수정
                        setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 수정")
                        binding.lLayoutTodoEdit.visibility = View.VISIBLE // 버튼 띄우기
                        binding.btnTodoCreate.visibility = View.GONE
                        isEdit = true

                        // json data 파싱하기 및 뷰 그리기
                        val jsonObject = JSONObject(data.toString())

                        // todoList data
                        val tempTodoList: String? = jsonObject.getString("todoList")
                        val todoJsonArray = JSONArray(tempTodoList)
                        val tempTodoSaveList = ArrayList<TodoItem>()
                        for (i in 0 until todoJsonArray.length()) {
                            val subJsonObject = todoJsonArray.getJSONObject(i)
                            val todoIdx = subJsonObject.getLong("todoIdx")
                            val todoName = subJsonObject.getString("todoName")

                            // 오늘 할 일 리스트에 정보 저장
                            val tempEdt = addEditText()
                            todoList.add(TodoCheckModifyItem(todoIdx, tempEdt, todoName))
                            todoList[i].edt.setText(todoName)
                            tempTodoSaveList.add(TodoItem(todoIdx, todoName, false))
                        }
                        addTodoName.clear()

                        // todoTagList data
                        val tempTodoTagList: JSONArray? = jsonObject.getJSONArray("todoTagList")
                        val tempTodoTagList2 = ArrayList<Int>()
                        for (i in 0 until (tempTodoTagList?.length() ?: 0)) {
                            // 일상
                            if (tempTodoTagList?.get(i) == 1) {
                                binding.cbTagDaily.isChecked = true
                                tempTodoTagList2.add(1)
                                binding.cbTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 직장
                            else if (tempTodoTagList?.get(i) == 2) {
                                binding.cbTagJob.isChecked = true
                                tempTodoTagList2.add(2)
                                binding.cbTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 취미
                            else if (tempTodoTagList?.get(i) == 3) {
                                binding.cbTagHobby.isChecked = true
                                tempTodoTagList2.add(3)
                                binding.cbTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 공부
                            else if (tempTodoTagList?.get(i) == 4) {
                                binding.cbTagStudy.isChecked = true
                                tempTodoTagList2.add(4)
                                binding.cbTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 운동
                            else if (tempTodoTagList?.get(i) == 5) {
                                binding.cbTagSports.isChecked = true
                                tempTodoTagList2.add(5)
                                binding.cbTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 독서
                            else if (tempTodoTagList?.get(i) == 6) {
                                binding.cbTagReading.isChecked = true
                                tempTodoTagList2.add(6)
                                binding.cbTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 여행
                            else if (tempTodoTagList?.get(i) == 7) {
                                binding.cbTagTravel.isChecked = true
                                tempTodoTagList2.add(7)
                                binding.cbTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 쇼핑
                            else if (tempTodoTagList?.get(i) == 8) {
                                binding.cbTagShopping.isChecked = true
                                tempTodoTagList2.add(8)
                                binding.cbTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                        }

                        // goOutTime data
                        val tempGoOutTime: String? = jsonObject.getString("goOutTime")
                        val goOutTime = LocalTime.parse(tempGoOutTime.toString())
                        binding.timepickerTodo.hour = goOutTime.hour
                        binding.timepickerTodo.minute = goOutTime.minute

                        // remindTime data
                        val tempRemindTime: JSONArray = jsonObject.getJSONArray("remindTime")
                        val tempRemindList = ArrayList<Int>()
                        for (i in 0 until tempRemindTime.length()) {
                            // 없음
                            if (tempRemindTime[i] == 1) {
                                binding.cbRemindNo.isChecked = true
                                tempRemindList.add(1)
                                binding.cbRemindNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 5분전
                            else if (tempRemindTime[i] == 2) {
                                binding.cbRemind5m.isChecked = true
                                tempRemindList.add(2)
                                binding.cbRemind5m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 30분전
                            else if (tempRemindTime[i] == 3) {
                                binding.cbRemind30m.isChecked = true
                                tempRemindList.add(3)
                                binding.cbRemind30m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 1시간전
                            else if (tempRemindTime[i] == 4) {
                                binding.cbRemind1h.isChecked = true
                                tempRemindList.add(4)
                                binding.cbRemind1h.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                            // 하루전
                            else if (tempRemindTime[i] == 5) {
                                binding.cbRemind1d.isChecked = true
                                tempRemindList.add(5)
                                binding.cbRemind1d.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                            }
                        }

                        // 선택한 날짜의 일정 데이터 리스트 저장
                        todoListItem = TodoListItem(
                            todoItem = tempTodoSaveList,
                            date = todoDate,
                            todoTag = tempTodoTagList2,
                            goOutTime = goOutTime,
                            remindTime = tempRemindList
                        )

                    } catch (e: JSONException) {
                        Log.w("TodoFragment - Get Todo", e.stackTraceToString())
                        showToast(resources.getString(R.string.toast_server_error)) // 실패
                    }
                }
                // 일정이 없다면(일정 추가 화면이라면)
                else {
                    // 일정 추가
                    setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 추가")
                    binding.btnTodoCreate.visibility = View.VISIBLE
                    binding.lLayoutTodoEdit.visibility = View.GONE
                    val tempEdt = addEditText()
                    todoList.add(TodoCheckModifyItem(null, tempEdt, null))
                    isEdit = false
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

    // 기존에 선택되어 있던 값 초기화
    private fun setClearView() {
        addTodoName.clear()
        deleteTodoIdx.clear()
        todoList.clear()
        tagList.clear()
        remindList.clear()
        binding.lLayoutTodo.removeAllViews()
        binding.cbTagShopping.isChecked = false
        binding.cbTagReading.isChecked = false
        binding.cbTagSports.isChecked = false
        binding.cbTagStudy.isChecked = false
        binding.cbTagHobby.isChecked = false
        binding.cbTagTravel.isChecked = false
        binding.cbTagDaily.isChecked = false
        binding.cbTagJob.isChecked = false
        binding.cbTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbRemindNo.isChecked = false
        binding.cbRemind5m.isChecked = false
        binding.cbRemind30m.isChecked = false
        binding.cbRemind1h.isChecked = false
        binding.cbRemind1d.isChecked = false
        binding.cbRemindNo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbRemind5m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbRemind30m.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbRemind1h.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        binding.cbRemind1d.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
    }
}