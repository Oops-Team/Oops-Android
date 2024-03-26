package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.ApplicationClass
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.databinding.FragmentWithdrawal2Binding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.clearToken

class Withdrawal2Fragment: BaseFragment<FragmentWithdrawal2Binding>(FragmentWithdrawal2Binding::inflate) {
    private lateinit var withdrawalItem: WithdrawalItem // 탈퇴 사유 데이터
    private var isReadTerms: Boolean = false // 개인정보 처리 방침 읽음 여부

    private var isEnable = false // 탈퇴할게요 버튼 활성화 여부

    override fun initViewCreated() {
        // 툴 바 텍스트 설정
        binding.toolbarWithdrawal2.tvSubToolbarTitle.text = getString(R.string.withdrawal_title)

        // 전달 받은 데이터 저장
        val args: Withdrawal2FragmentArgs by navArgs()
        try {
            withdrawalItem = args.withdrawalItem!!
            isReadTerms = args.isReadTerms

        } catch (e: Exception) {
            Log.e("Withdrawal2Fragment - Nav Item", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭 이벤트
        binding.toolbarWithdrawal2.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // TermsFragment 화면에서 뒤로가기 버튼을 클릭한 경우
        if (binding.cBoxWithdrawal2Check1.isChecked) {
            binding.btnWithdrawal2End.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal2End.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }

        // 개인 정보 처리 방침 버튼 클릭 이벤트
        binding.lLayoutWithdrawal2Terms.setOnClickListener {
            val actionToTerms: NavDirections = Withdrawal2FragmentDirections.actionWithdrawal2FrmToTermsFrm(
                    withdrawalItem
                )
            view?.findNavController()?.navigate(actionToTerms) // 개인 정보 처리 방침 화면으로 이동
        }

        // 개인정보 처리 방침을 확인했다면
        if (isReadTerms) {
            // 처리 방침 상태 변경
            binding.cBoxWithdrawal2Check1.isChecked = true
            binding.cBoxWithdrawal2Check2.isChecked = true
            binding.cBoxWithdrawal2Check3.isChecked = true

            // 버튼 활성화
            updateButtonUI()
        }

        /* 체크박스 3개 클릭 이벤트 */
        binding.cBoxWithdrawal2Check1.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check1.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check1.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check1.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        binding.cBoxWithdrawal2Check2.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check2.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check2.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check2.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        binding.cBoxWithdrawal2Check3.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check3.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check3.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check3.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        // 탈퇴할게요 버튼 클릭
        binding.btnWithdrawal2End.setOnClickListener {
            if (isEnable) {
                // 전달 받은 데이터 출력
                Log.d("Withdrawal2Fragment - api 전달할 데이터", withdrawalItem.toString())

                // 토큰 삭제
                clearToken()

                // 유저 데이터 삭제
                val userDB = AppDatabase.getUserDB()!! // room db의 user db
                userDB.userDao().deleteAllUser()

                // TODO: 회원 탈퇴 API 연동

                // 로그인 화면으로 이동
                val actionToLogin = Withdrawal2FragmentDirections.actionWithdrawal2FrmToLoginActivity()
                view?.findNavController()?.navigate(actionToLogin)
            }
        }
    }

    // 탈퇴할게요 버튼 활성화 여부 체크 함수
    private fun updateButtonUI() {
        // 버튼 색상 전환
        isEnable = true
        ButtonUtils().setColorAnimation(binding.btnWithdrawal2End)
    }

    // 탈퇴할게요 버튼 활성화 로직 체크 함수
    private fun checkButtonCondition() {
        // 모든 버튼이 체크가 되어 있고, 개인정보 처리 방침을 확인했다면
        if (binding.cBoxWithdrawal2Check1.isChecked && binding.cBoxWithdrawal2Check2.isChecked &&
            binding.cBoxWithdrawal2Check3.isChecked && isReadTerms) {
            if (!isEnable) {
                updateButtonUI() // 색상 전환
            }
        }
        // 아니라면
        else {
            // 색상 바꾸기 및 버튼 비활성화
            isEnable = false
            binding.btnWithdrawal2End.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnWithdrawal2End.setBackgroundColor(ApplicationClass.applicationContext().getColor(R.color.Gray_100))
        }
    }
}