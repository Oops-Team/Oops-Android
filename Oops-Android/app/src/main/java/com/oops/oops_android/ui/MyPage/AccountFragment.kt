package com.oops.oops_android.ui.MyPage

import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentAccountBinding
import com.oops.oops_android.ui.Base.BaseFragment

// 계정 관리 화면
class AccountFragment: BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {
    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        binding.toolbarAccount.tvSubToolbarTitle.text = getString(R.string.myPage_account) // 툴바 설정
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarAccount.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 회원 탈퇴 버튼 클릭
        binding.tvAccountWithdrawal.setOnClickListener {
            // 회원 탈퇴 화면 1로 이동
            val actionToWithdrawal1: NavDirections = AccountFragmentDirections.actionAccountFrmToWithdrawal1Frm()
            view?.findNavController()?.navigate(actionToWithdrawal1)
        }
    }
}