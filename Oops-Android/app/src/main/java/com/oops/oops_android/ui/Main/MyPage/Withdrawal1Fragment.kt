package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentWithdrawal1Binding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.ButtonUtils
import java.lang.Exception

/* 회원 탈퇴 화면 1 */
class Withdrawal1Fragment: BaseFragment<FragmentWithdrawal1Binding>(FragmentWithdrawal1Binding::inflate) {
    private lateinit var item: WithdrawalItem // 서버에서 전해 줄 회원 탈퇴 데이터
    private var loginType = "" // 회원 탈퇴하려고 하는 계정 유형
    private var isSelectedReason5 = false // 5번 라디오 버튼 클릭 여부
    private var isEnable = false // 탈퇴할게요 버튼 활성화 여부

    override fun initViewCreated() {
        // 툴 바 제목 숨기기
        binding.toolbarWithdrawal1.tvSubToolbarTitle.visibility = View.GONE

        // 로그인 유형 받기
        try {
            val args: Withdrawal1FragmentArgs by navArgs()
            loginType = args.loginId
        }
        catch (e: Exception) {
            Log.e("Withdrawal1Fragment - Get Nav Data", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarWithdrawal1.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // Withdrawal2Fragment 화면에서 뒤로가기를 눌렀을 경우(isEnable = true)
        if (binding.rBtnWithdrawal1Reason1.isChecked) {
            binding.btnWithdrawal1Next.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal1Next.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }
        else if (binding.rBtnWithdrawal1Reason2.isChecked) {
            binding.btnWithdrawal1Next.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal1Next.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }
        else if (binding.rBtnWithdrawal1Reason3.isChecked) {
            binding.btnWithdrawal1Next.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal1Next.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }
        else if (binding.rBtnWithdrawal1Reason4.isChecked) {
            binding.btnWithdrawal1Next.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal1Next.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }
        else if (binding.rBtnWithdrawal1Reason5.isChecked) {
            binding.btnWithdrawal1Next.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal1Next.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }

        // 라디오 그룹 내의 라디오 버튼 클릭 여부
        binding.rGroupWithdrawal1Reason.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rBtnWithdrawal1Reason1.id -> {
                    isSelectedReason5 = false
                    if (binding.rBtnWithdrawal1Reason1.isChecked) {
                        item = WithdrawalItem(1)
                    }
                }

                binding.rBtnWithdrawal1Reason2.id -> {
                    isSelectedReason5 = false
                    if (binding.rBtnWithdrawal1Reason2.isChecked) {
                        item = WithdrawalItem(2)
                    }
                }

                binding.rBtnWithdrawal1Reason3.id -> {
                    isSelectedReason5 = false
                    if (binding.rBtnWithdrawal1Reason3.isChecked) {
                        item = WithdrawalItem(3)
                    }
                }

                binding.rBtnWithdrawal1Reason4.id -> {
                    isSelectedReason5 = false
                    if (binding.rBtnWithdrawal1Reason4.isChecked) {
                        item = WithdrawalItem(4)
                    }
                }

                binding.rBtnWithdrawal1Reason5.id -> {
                    if (binding.rBtnWithdrawal1Reason5.isChecked) {
                        isSelectedReason5 = true
                        binding.edtWithdrawal1Etc.visibility = View.VISIBLE
                        item = WithdrawalItem(5)
                    }
                }
            }

            // 버튼 활성화 상태가 아닌 경우
            if (!isEnable) {
                updateButtonUI() // 버튼 색상 전환
            }

            // 5번 라디오 버튼이 안 클릭되어 있다면
            if (!isSelectedReason5) {
                binding.edtWithdrawal1Etc.visibility = View.INVISIBLE // edt 숨기기
            }
        }

        // 탈퇴 할게요 버튼 클릭 이벤트
        binding.btnWithdrawal1Next.setOnClickListener {
            // 버튼이 활성화 된 경우
            if (isEnable) {
                // 기타에 내용이 작성되었다면
                if (isSelectedReason5) {
                    item.subReason = binding.edtWithdrawal1Etc.text.toString()
                }

                // 회원 탈퇴 2 화면으로 이동
                val actionToWithdrawal2: NavDirections = Withdrawal1FragmentDirections.actionWithdrawal1FrmToWithdrawal2Frm(
                        item,
                        false,
                        loginType
                    )
                view?.findNavController()?.navigate(actionToWithdrawal2)
            }
        }
    }

    // 탈퇴할게요 버튼 활성화 여부 체크 함수
    private fun updateButtonUI() {
        // 버튼 색상 전환
        isEnable = true
        ButtonUtils().setColorAnimation(binding.btnWithdrawal1Next)
    }
}