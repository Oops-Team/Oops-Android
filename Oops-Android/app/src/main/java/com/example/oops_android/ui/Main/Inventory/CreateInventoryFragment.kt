package com.example.oops_android.ui.Main.Inventory

import android.content.res.ColorStateList
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

    private var tagList = ArrayList<Long>() // 추가된 일정 리스트
    private var isEnable = false // 소지품 추가 버튼 클릭 가능 여부
    private var isOverlap = true // 인벤토리 이름 중복 여부

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
                binding.cvCreateInventoryStuffAdd.visibility = View.GONE
                binding.lLayoutCreateInventoryEdit.visibility = View.VISIBLE
                binding.toolbarCreateInventory.ivSubToolbarRight.visibility = View.VISIBLE
            }
        }
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

        // 소지품 추가 버튼 동작 가능 여부 확인
        binding.edtCreateInventoryName.onTextChanged {
            updateButtonUI()

            // 인벤토리 이름이 중복이라면
            if (isOverlap) {
                // 인벤토리 경고 문구 숨기기
                binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_bg)
                binding.tvCreateInventoryNameAlert.visibility = View.GONE
                isOverlap = false
            }
        }

        // 소지품 추가 버튼 클릭 이벤트
        binding.btnCreateInventoryStuffAdd.setOnClickListener {
            if (isEnable) {
                // TODO: API 연동 필요

                // TODO: 만약, 인벤토리 이름이 중복이라면
                if (isOverlap) {
                    binding.edtCreateInventoryName.setBackgroundResource(R.drawable.inventory_edit_text_error_bg)
                    binding.tvCreateInventoryNameAlert.visibility = View.VISIBLE
                }
            }
        }
    }

    // 소지품 추가 버튼 동작 가능 여부 확인 함수
    private fun updateButtonUI() {
        // 값이 모두 입력되어 있다면
        if (binding.edtCreateInventoryName.text.isNotEmpty() && tagList.isNotEmpty()) {
            // 버튼이 활성화 상태가 아니라면
            if (!isEnable) {
                ButtonUtils().setAllColorAnimation(binding.btnCreateInventoryStuffAdd)
                isEnable = true
            }
        }
        // 값이 하나라도 안 입력되어 있다면
        else {
            // 소지품 추가 버튼 비활성화
            binding.btnCreateInventoryStuffAdd.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnCreateInventoryStuffAdd.setBackgroundColor(applicationContext().getColor(R.color.Gray_100))

            isEnable = false
        }
    }

    // 태그 클릭 이벤트 연결 함수
    private fun setOnCheckedChanged(checkBox: CompoundButton) {
        checkBox.setOnCheckedChangeListener { view, isChecked ->
            onCheckedChanged(view, isChecked)
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