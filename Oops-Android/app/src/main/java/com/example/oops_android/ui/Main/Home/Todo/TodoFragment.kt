package com.example.oops_android.ui.Main.Home.Todo

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputFilter
import android.text.InputType
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
import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentTodoBinding
import com.example.oops_android.ui.Base.BaseFragment

// 일정 추가 & 수정 화면
// TODO: 일정 추가 or 일정 수정에 맞게 화면 세팅 필요
class TodoFragment: BaseFragment<FragmentTodoBinding>(FragmentTodoBinding::inflate), CompoundButton.OnCheckedChangeListener {
    private var remindList = ArrayList<Long>() // 선택된 알림 시간 리스트
    private var edtCount = 1 // 추가된 edittext 갯수
    private var todoList = ArrayList<EditText>() // 추가된 일정 리스트
    private var isShowDeleteBtn = false // EditText의 삭제 버튼 보임 여부

    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        // 툴 바 제목 설정
        setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 추가")

        // TODO: 날짜 정보 불러오기(일정 추가 or 일정 수정)
    }

    override fun initAfterBinding() {
        // 일정 리스트에 기본 EditText 추가
        todoList.add(binding.edtTodo)

        // 월간 캘린더
        binding.mcvTodoCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기

        // 뒤로 가기 버튼 클릭
        binding.toolbarTodo.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 오늘 할 일 추가 버튼을 누른 경우
        binding.iBtnTodo.setOnClickListener {
            // 30개 이상이라면
            if (edtCount >= 30) {
                showCustomSnackBar(R.string.toast_todo_not_add) // 스낵바 띄우기
            }
            else {
                ++edtCount // 추가된 edittext 갯수 세기

                // 동적으로 EditText 추가하기
                val edtView = EditText(requireContext())
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                params.setMargins(0, 16, 0, 0)
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
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_300))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL) // 기본값 사용
                    layoutParams = params
                }
                binding.lLayoutTodo.addView(edtView) // 레이아웃에 EditText 추가
                todoList.add(edtView) // 리스트에 EditText 추가

                // 동적 생성한 EditText에 대한 입력 처리
                setOtherEdtEvent(edtView)
            }
        }

        // 가장 첫번째 있는 EditText박스에 대한 입력 처리
        setFirstEdtEvent()

        // 휴지통 버튼을 누른 경우
        binding.ivTodoDelete.setOnClickListener {
            clickDeleteBtn()
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

        // TODO: 푸시알림 off인 상태에서 클릭 시 7번 클릭하였을 때 푸시 알림 on 유도 팝업 노출
        // 알림 시간 선택한 경우에 따른 이벤트 처리
        // 없음을 선택한 경우
        binding.cbRemindNo.setOnCheckedChangeListener { checkBox, isChecked ->
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
    }

    // 가장 첫번째에 있는 EditText에 대한 입력 처리
    private fun setFirstEdtEvent() {
        // 가장 첫번째에 있는 EditText에서 완료 버튼을 눌렀다면
        binding.edtTodo.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 일정에 값이 없는 경우 EditText 삭제
                    if (binding.edtTodo.text.toString().isBlank() && todoList.size > 1) {
                        todoList.remove(binding.edtTodo)
                        binding.edtTodo.visibility = View.GONE
                    }
                    getHideKeyboard(binding.root)
                    return true
                }
                return false
            }
        })

        // 가장 첫번째에 있는 EditText에 Focus가 되어 있다면
        binding.edtTodo.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 화면 바깥을 클릭한 경우
                binding.lLayoutTodoTop.setOnClickListener {
                    // 일정에 값이 없는 경우 EditText 숨기기
                    if (binding.edtTodo.text.toString().isBlank() && todoList.size > 1) {
                        todoList.remove(binding.edtTodo)
                        binding.edtTodo.visibility = View.GONE
                    }
                    getHideKeyboard(binding.root)
                }
            }
        }
    }

    // 동적 생성한 EditText에 대한 입력 처리
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
                        todoList.remove(edtView)
                        binding.lLayoutTodo.removeView(edtView)
                    }
                    getHideKeyboard(binding.root)
                    return true
                }
                return false
            }
        })

        // EditText에 Focus가 되어 있다면
        edtView.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 화면 바깥을 클릭한 경우
                binding.lLayoutTodoTop.setOnClickListener {
                    // 일정에 값이 없는 경우 EditText 삭제
                    if (edtView.text.toString().isBlank() && todoList.size > 1) {
                        todoList.remove(edtView) // 리스트에서 요소 삭제
                        binding.lLayoutTodo.removeView(edtView)
                    }
                    getHideKeyboard(binding.root)
                }
            }
        }
    }

    // 휴지통 클릭 이벤트 처리 함수
    @SuppressLint("ClickableViewAccessibility")
    private fun clickDeleteBtn() {
        if (isShowDeleteBtn) {
            for (i in 0 until todoList.size) {
                // 각 EditText의 delete 버튼이 보인다면
                todoList[i].setCompoundDrawables(null, null, null, null) // delete 버튼 숨기기
                isShowDeleteBtn = false
            }
        }
        // 각 EditText의 delete 버튼이 안 보인다면
        else {
            // EditText의 삭제 버튼 띄우기
            for (i in 0 until todoList.size) {
                val deleteBtn = ContextCompat.getDrawable(requireContext(), R.drawable.ic_todo_delete_24)
                todoList[i].setCompoundDrawablesWithIntrinsicBounds(null, null, deleteBtn, null) // delete 버튼 보이기
                isShowDeleteBtn = true

                // 삭제 버튼을 클릭한 경우
                todoList[i].setOnTouchListener(View.OnTouchListener { _, event ->
                    val DRAWABLE_RIGHT = 2

                    try {
                        if (event.action == MotionEvent.ACTION_UP) {
                            if (event.rawX >= (todoList[i].right -
                                        todoList[i].compoundDrawables[DRAWABLE_RIGHT].bounds.width() - 12)) {

                                // 일정이 2개 이상이라면
                                if (todoList.size > 1) {
                                    // EditText 삭제
                                    todoList.remove(todoList[i])

                                    // 첫번째 EditText가 아니라면
                                    if (i != 0) {
                                        // 뷰 삭제
                                        binding.lLayoutTodo.removeViewAt(i - 1)
                                    }
                                    // 첫번째 EditText라면
                                    else {
                                        binding.edtTodo.visibility = View.GONE
                                    }
                                }
                                // 일정이 1개라면
                                else {
                                    // todo: 삭제 안된다는 스낵바 띄우기 디자인 작업 요청해보기
                                    showCustomSnackBar(R.string.toast_todo_not_delete)
                                }
                                return@OnTouchListener true
                            }
                        }
                        false
                    } catch (e: Exception) {
                        false
                    }
                })
            }
        }
    }

    // 알림 시간 선택 이벤트 처리 함수
    private fun setRemindCheckedChanged(checkBox: CompoundButton, isChecked: Boolean, itemCode: Long) {
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
    }

    // 태그 선택 이벤트 연결 함수
    private fun setOnCheckedChanged(checkBox: CompoundButton) {
        checkBox.setOnCheckedChangeListener { view, isChecked ->
            onCheckedChanged(view, isChecked)
        }
    }

    // 태그 및 알림 시간 선택 이벤트
    override fun onCheckedChanged(checkBox: CompoundButton, isChecked: Boolean) {
        var tagList = ArrayList<Long>() // 선택된 태그 리스트
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
}