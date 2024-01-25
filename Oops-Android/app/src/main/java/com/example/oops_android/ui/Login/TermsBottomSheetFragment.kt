package com.example.oops_android.ui.Login

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentTermsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 개인정보 수집 및 이용 동의 약관 바텀 시트
class TermsBottomSheetFragment(val itemClick: (Int, Boolean) -> Unit): BottomSheetDialogFragment() {

    // 뷰바인딩
    private var mBinding: FragmentTermsBottomSheetBinding? = null
    private val binding get() = mBinding!!

    // 버튼 값 변경 여부
    private var isVitalCheck: Boolean = false
    private var isChoiceCheck: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = FragmentTermsBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // dim 제거
        // 화면 바깥 터치 막기 & 드래그 막기
        dialog.apply {
            setOnShowListener {
                window?.findViewById<View>(com.google.android.material.R.id.touch_outside)?.setOnClickListener(null)
                (window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.layoutParams as CoordinatorLayout.LayoutParams).behavior = null
            }
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 개인정보 수집 및 이용 동의 버튼을 클릭한 경우
        binding.ivTermsBottomSheetVital.setOnClickListener {
            isVitalCheck = true
            binding.ivTermsBottomSheetVital.imageTintList = ColorStateList.valueOf(Color.parseColor("#5F77E1")) // 아이콘 보라색으로

            // 버튼 색상 바꾸기
            binding.btnTermsBottomSheetNext.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Main_500))
            binding.btnTermsBottomSheetNext.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))
        }

        // 개인정보 수집 및 이용 동의 텍스트를 클릭한 경우
        binding.tvTermsBottomSheetVital.setOnClickListener {
            itemClick(0, false)
        }

        // 앱 푸시 알림 동의 버튼을 클릭한 경우
        binding.ivTermsBottomSheetChoice.setOnClickListener {
            if (isChoiceCheck)
                binding.ivTermsBottomSheetChoice.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Main_500))
            else
                binding.ivTermsBottomSheetChoice.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_300))

            isChoiceCheck = !isChoiceCheck
        }

        // 다음 버튼을 클릭한 경우
        binding.btnTermsBottomSheetNext.setOnClickListener {
            if (isVitalCheck)
                itemClick(1, isChoiceCheck)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}