package com.oops.oops_android.ui.Login

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentTermsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oops.oops_android.utils.setOnSingleClickListener

// 개인정보 수집 및 이용 동의 약관 바텀 시트
class TermsBottomSheetFragment(val itemClick: (Int) -> Unit): BottomSheetDialogFragment() {

    // 뷰바인딩
    private var mBinding: FragmentTermsBottomSheetBinding? = null
    private val binding get() = mBinding!!

    // 버튼 값 변경 여부
    private var isVitalCheck: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = FragmentTermsBottomSheetBinding.inflate(inflater, container, false)

        // 핸드폰 내의 뒤로가기 버튼 클릭 이벤트 막기
        dialog?.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }

        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // dim 제거

        dialog.apply {
            setOnShowListener {
                setCanceledOnTouchOutside(false) // 화면 바깥 터치 시 바텀 시트 닫치지 않도록 설정
                behavior.isDraggable = false // 드래그 시 바텀 시트 닫치지 않도록 설정
            }
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 개인정보 수집 및 이용 동의 버튼을 클릭한 경우
        binding.ivTermsBottomSheetVital.setOnClickListener {
            isVitalCheck = !isVitalCheck
            if (isVitalCheck) {
                binding.ivTermsBottomSheetVital.setImageResource(R.drawable.ic_confirm_25) // 아이콘 바꾸기

                // 버튼 색상 바꾸기
                binding.btnTermsBottomSheetNext.setTextAppearance(R.style.SemiWideButtonEnableStyle)
                binding.btnTermsBottomSheetNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
            }
            else {
                binding.ivTermsBottomSheetVital.setImageResource(R.drawable.ic_check_24) // 아이콘 바꾸기

                // 버튼 색상 바꾸기
                binding.btnTermsBottomSheetNext.setTextAppearance(R.style.SemiWideButtonDisableStyle)
                binding.btnTermsBottomSheetNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray_100))
            }
        }

        // 개인정보 수집 및 이용 동의 텍스트를 클릭한 경우
        binding.tvTermsBottomSheetVital.setOnClickListener {
            itemClick(0)
        }

        // 다음 버튼을 클릭한 경우
        binding.btnTermsBottomSheetNext.setOnSingleClickListener {
            if (isVitalCheck)
                itemClick(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}