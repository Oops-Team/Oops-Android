package com.example.oops_android.ui.Main.Inventory

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentCreateInventoryBinding
import com.example.oops_android.ui.Base.BaseFragment
import com.example.oops_android.utils.ButtonUtils
import com.example.oops_android.utils.onTextChanged
import com.example.oops_android.ApplicationClass.Companion.applicationContext

/* 인벤토리 생성 & 수정 화면 */
class CreateInventoryFragment: BaseFragment<FragmentCreateInventoryBinding>(FragmentCreateInventoryBinding::inflate), CompoundButton.OnCheckedChangeListener {

    private var tagList = ArrayList<Int>() // 추가된 일정 리스트
    private var isEnable = false // 소지품 추가 버튼 클릭 가능 여부
    private var isOverlapName = false // 인벤토리 이름 중복 여부
    private var isChangeName = false // 인벤토리 이름 변경 여부(수정 화면)
    private var isOverlapTag = false // 인벤토리 태그 중복 여부

    private var inventoryList = ArrayList<CategoryItemUI>() // 전체 인벤토리 리스트
    private lateinit var inventoryItem: CategoryItemUI // 수정할 인벤토리 아이템
    private var isShowEdit = false // 현재 화면이 어떤 화면인지에 대한 여부

    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        val args: CreateInventoryFragmentArgs by navArgs()
        when (args.inventoryDivision) {
            // 툴 바 제목 설정 및 버튼 띄우기
            "InventoryCreate" -> {
                setToolbarTitle(binding.toolbarCreateInventory.tvSubToolbarTitle, "인벤토리 생성")
                binding.cvCreateInventoryStuffAdd.visibility = View.VISIBLE
            }
            "InventoryEdit" -> {
                setToolbarTitle(binding.toolbarCreateInventory.tvSubToolbarTitle, "인벤토리 수정")
                binding.lLayoutCreateInventoryEdit.visibility = View.VISIBLE
                binding.toolbarCreateInventory.ivSubToolbarRight.visibility = View.VISIBLE

                // 전달받은 아이템 값 적용
                inventoryItem = args.inventoryItem!!

                // 뷰 그리기
                binding.edtCreateInventoryName.setText(inventoryItem.inventoryName)
                setOnTag(inventoryItem.inventoryTag)

                isShowEdit = true
            }
        }

        // 전달받은 리스트 값 적용
        inventoryList = args.inventoryList
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarCreateInventory.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 화면 터치 시 키보드 숨기기
        binding.lLayoutCreateInventoryTop.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 태그 클릭 이벤트
        setOnCheckedChanged(binding.cbInventoryTagDaily)
        setOnCheckedChanged(binding.cbInventoryTagJob)
        setOnCheckedChanged(binding.cbInventoryTagHobby)
        setOnCheckedChanged(binding.cbInventoryTagStudy)
        setOnCheckedChanged(binding.cbInventoryTagSports)
        setOnCheckedChanged(binding.cbInventoryTagReading)
        setOnCheckedChanged(binding.cbInventoryTagTravel)
        setOnCheckedChanged(binding.cbInventoryTagShopping)

        // 소지품 추가 & 인벤토리 수정 버튼 동작 가능 여부 확인
        binding.edtCreateInventoryName.onTextChanged {
            updateButtonUI()
        }

        // 소지품 추가 버튼 클릭 이벤트
        binding.btnCreateInventoryStuffAdd.setOnClickListener {
            if (isEnable) {
                // TODO: API 연동 필요
                showToast("소지품 추가 화면으로 이동!")
            }
        }

        // 수정 완료 버튼 클릭 이벤트
        binding.btnCreateInventoryEdit.setOnClickListener {
            if (isEnable) {
                // TODO: API 연동 필요
                showToast("인벤토리 화면으로 이동!")
                view?.findNavController()?.popBackStack()
            }
        }
    }

    // 소지품 추가 또는 인벤토리 수정 버튼 동작 가능 여부 확인 함수
    private fun updateButtonUI() {
        // 일정 추가 화면의 경우
        if (!isShowEdit) {
            // 기존 인벤토리 이름 리스트와 작성한 이름이 중복이라면
            if (isValueInList(inventoryList, binding.edtCreateInventoryName.text.toString())) {
                // 인벤토리 경고 문구 띄우기
                binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_error_bg)
                binding.tvCreateInventoryNameAlert.visibility = View.VISIBLE
                isOverlapName = true
                isEnable = false
            }
            else {
                // 인벤토리 경고 문구 숨기기
                binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_bg)
                binding.tvCreateInventoryNameAlert.visibility = View.GONE
                isOverlapName = false
            }

            // 모든 값이 입력되어 있다면
            if (binding.edtCreateInventoryName.text.toString().isNotEmpty() && tagList.isNotEmpty()) {
                // 이름 값 중복이 아니라면
                if (!isOverlapName) {
                    // 버튼 활성화 상태가 아니라면 값 바꾸기
                    if (!isEnable) {
                        ButtonUtils().setAllColorAnimation(binding.btnCreateInventoryStuffAdd)
                        isEnable = true
                    }
                }
                else {
                    // 버튼 활성화 해제
                    binding.btnCreateInventoryStuffAdd.setTextAppearance(R.style.WideButtonDisableStyle)
                    binding.btnCreateInventoryStuffAdd.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
                    isEnable = false
                }
            }
            else {
                // 버튼 활성화 해제
                binding.btnCreateInventoryStuffAdd.setTextAppearance(R.style.WideButtonDisableStyle)
                binding.btnCreateInventoryStuffAdd.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
                isEnable = false
            }
        }
        // 일정 수정 화면의 경우
        else {
            // item의 인벤토리 이름을 제외한 다른 인벤토리 이름과 작성한 인벤토리 이름이 중복이라면
            if (isValueInList(inventoryList, binding.edtCreateInventoryName.text.toString())) {
                if (inventoryItem.inventoryName != binding.edtCreateInventoryName.text.toString()) {
                    // 인벤토리 경고 문구 띄우기
                    binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_error_bg)
                    binding.tvCreateInventoryNameAlert.visibility = View.VISIBLE
                    isOverlapName = true
                    isEnable = false
                }
                // item의 이름과 작성한 인벤토리 이름이 같다면
                else {
                    isChangeName = false
                }
            }
            else {
                // 인벤토리 경고 문구 숨기기
                binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_bg)
                binding.tvCreateInventoryNameAlert.visibility = View.GONE
                isOverlapName = false
                isChangeName = true
            }

            // item의 태그와 선택한 태그가 중복이라면(= 값이 안 바뀌었다면)
            isOverlapTag = compareLists(inventoryItem.inventoryTag, tagList)

            // 모든 값이 입력되어 있다면
            if (binding.edtCreateInventoryName.text.toString().isNotEmpty() && tagList.isNotEmpty()) {
                // 이름값&태그 또는 이름값만 중복이라면
                if ((isOverlapName && isOverlapTag) || (!isChangeName && isOverlapTag) || isOverlapName) {
                    // 버튼 활성화 해제
                    binding.btnCreateInventoryEdit.setTextAppearance(R.style.WideButtonDisableStyle)
                    binding.btnCreateInventoryEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
                    isEnable = false
                }
                // 중복이 아니라면
                else {
                    // 버튼 활성화 상태가 아니라면 값 바꾸기
                    if (!isEnable) {
                        ButtonUtils().setAllColorAnimation(binding.btnCreateInventoryEdit)
                        isEnable = true
                    }
                }
            }
            else {
                // 버튼 활성화 해제
                binding.btnCreateInventoryEdit.setTextAppearance(R.style.WideButtonDisableStyle)
                binding.btnCreateInventoryEdit.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))
                isEnable = false
            }
        }
    }

    // 배열에 특정 값이 있는 지 확인하는 함수
    private fun isValueInList(inventoryList: ArrayList<CategoryItemUI>, name: String): Boolean {
        return inventoryList.any { item ->
            item.inventoryName == name
        }
    }

    // 두 개의 리스트의 값이 같은지 확인하는 함수
    private fun compareLists(inventoryTag: ArrayList<Int>?, tagList: ArrayList<Int>): Boolean {
        // 두 리스트의 크기가 다르다면
        if (inventoryTag!!.size != tagList.size) {
            return false
        }

        val result = inventoryTag.zip(tagList)

        // 두 리스트의 값이 다르다면
        for ((item1, item2) in result) {
            if (item1 != item2) {
                return false
            }
        }

        // 위의 2가지 조건을 모두 통과했다면 값 같음
        return true
    }

    // 태그 클릭 이벤트 연결 함수
    private fun setOnCheckedChanged(checkBox: CompoundButton) {
        checkBox.setOnCheckedChangeListener { view, isChecked ->
            onCheckedChanged(view, isChecked)
        }
    }

    // 태그 default 값 적용 함수
    private fun setOnTag(inventoryTag: ArrayList<Int>?) {
        tagList.clear()
        var tagCount = 0 // 선택된 태그 갯수

        // 체크 박스를 선택 했다면 리스트에 데이터 추가
        for (index in 0 until (inventoryTag?.size ?: 0)) {
            when (inventoryTag?.get(index)) {
                1 -> { // 일상
                    tagList.add(1)
                    binding.cbInventoryTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagDaily.isChecked = true
                }
                2 -> { // 직장
                    tagList.add(2)
                    binding.cbInventoryTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagJob.isChecked = true
                }
                3 -> { // 취미
                    tagList.add(3)
                    binding.cbInventoryTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagHobby.isChecked = true
                }
                4 -> { // 공부
                    tagList.add(4)
                    binding.cbInventoryTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagStudy.isChecked = true
                }
                5 -> { // 운동
                    tagList.add(5)
                    binding.cbInventoryTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagSports.isChecked = true
                }
                6 -> { // 독서
                    tagList.add(6)
                    binding.cbInventoryTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagReading.isChecked = true
                }
                7 -> { // 여행
                    tagList.add(7)
                    binding.cbInventoryTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagTravel.isChecked = true
                }
                8 -> { // 쇼핑
                    tagList.add(8)
                    binding.cbInventoryTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
                    binding.cbInventoryTagShopping.isChecked = true
                }
            }
        }

        // 선택된 데이터 구하기
        for (i in 0 until tagList.size) {
            ++tagCount // count 개수 세기

            // tagCount 개수가 3개 이상 이라면
            if (tagCount >= 3) {
                // 선택되지 않은 체크박스를 비활성화 시키기
                if (!binding.cbInventoryTagDaily.isChecked) {
                    binding.cbInventoryTagDaily.isEnabled = false
                }
                if (!binding.cbInventoryTagJob.isChecked) {
                    binding.cbInventoryTagJob.isEnabled = false
                }
                if (!binding.cbInventoryTagHobby.isChecked) {
                    binding.cbInventoryTagHobby.isEnabled = false
                }
                if (!binding.cbInventoryTagStudy.isChecked) {
                    binding.cbInventoryTagStudy.isEnabled = false
                }
                if (!binding.cbInventoryTagSports.isChecked) {
                    binding.cbInventoryTagSports.isEnabled = false
                }
                if (!binding.cbInventoryTagReading.isChecked) {
                    binding.cbInventoryTagReading.isEnabled = false
                }
                if (!binding.cbInventoryTagTravel.isChecked) {
                    binding.cbInventoryTagTravel.isEnabled = false
                }
                if (!binding.cbInventoryTagShopping.isChecked) {
                    binding.cbInventoryTagShopping.isEnabled = false
                }
            }
            // tagCount 개수가 3개 미만 이라면
            else {
                // 모든 체크박스 활성화
                binding.cbInventoryTagDaily.isEnabled = true
                binding.cbInventoryTagJob.isEnabled = true
                binding.cbInventoryTagHobby.isEnabled = true
                binding.cbInventoryTagStudy.isEnabled = true
                binding.cbInventoryTagSports.isEnabled = true
                binding.cbInventoryTagReading.isEnabled = true
                binding.cbInventoryTagTravel.isEnabled = true
                binding.cbInventoryTagShopping.isEnabled = true
            }
        }
    }

    // 태그 클릭 이벤트
    override fun onCheckedChanged(checkBox: CompoundButton, isChecked: Boolean) {
        tagList.clear()
        var tagCount = 0 // 선택된 태그 갯수

        // 체크 박스를 선택 했다면 리스트에 데이터 추가
        if (binding.cbInventoryTagDaily.isChecked) {
            tagList.add(1)
            binding.cbInventoryTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagDaily.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagJob.isChecked) {
            tagList.add(2)
            binding.cbInventoryTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagJob.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagHobby.isChecked) {
            tagList.add(3)
            binding.cbInventoryTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagHobby.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagStudy.isChecked) {
            tagList.add(4)
            binding.cbInventoryTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagStudy.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagSports.isChecked) {
            tagList.add(5)
            binding.cbInventoryTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagSports.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagReading.isChecked) {
            tagList.add(6)
            binding.cbInventoryTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagReading.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagTravel.isChecked) {
            tagList.add(7)
            binding.cbInventoryTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagTravel.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }
        if (binding.cbInventoryTagShopping.isChecked) {
            tagList.add(8)
            binding.cbInventoryTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.White)))
        } else {
            binding.cbInventoryTagShopping.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_400)))
        }

        // 선택된 데이터 구하기
        for (i in 0 until tagList.size) {
            ++tagCount // count 개수 세기

            // tagCount 개수가 3개 이상 이라면
            if (tagCount >= 3) {
                // 선택되지 않은 체크박스를 비활성화 시키기
                if (!binding.cbInventoryTagDaily.isChecked) {
                    binding.cbInventoryTagDaily.isEnabled = false
                }
                if (!binding.cbInventoryTagJob.isChecked) {
                    binding.cbInventoryTagJob.isEnabled = false
                }
                if (!binding.cbInventoryTagHobby.isChecked) {
                    binding.cbInventoryTagHobby.isEnabled = false
                }
                if (!binding.cbInventoryTagStudy.isChecked) {
                    binding.cbInventoryTagStudy.isEnabled = false
                }
                if (!binding.cbInventoryTagSports.isChecked) {
                    binding.cbInventoryTagSports.isEnabled = false
                }
                if (!binding.cbInventoryTagReading.isChecked) {
                    binding.cbInventoryTagReading.isEnabled = false
                }
                if (!binding.cbInventoryTagTravel.isChecked) {
                    binding.cbInventoryTagTravel.isEnabled = false
                }
                if (!binding.cbInventoryTagShopping.isChecked) {
                    binding.cbInventoryTagShopping.isEnabled = false
                }
            }
            // tagCount 개수가 3개 미만 이라면
            else {
                // 모든 체크박스 활성화
                binding.cbInventoryTagDaily.isEnabled = true
                binding.cbInventoryTagJob.isEnabled = true
                binding.cbInventoryTagHobby.isEnabled = true
                binding.cbInventoryTagStudy.isEnabled = true
                binding.cbInventoryTagSports.isEnabled = true
                binding.cbInventoryTagReading.isEnabled = true
                binding.cbInventoryTagTravel.isEnabled = true
                binding.cbInventoryTagShopping.isEnabled = true
            }
        }
        updateButtonUI()
    }
}