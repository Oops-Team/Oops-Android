package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.databinding.FragmentAccountBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.getToken

// 계정 관리 화면
class AccountFragment: BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate), CommonView {
    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        binding.toolbarAccount.tvSubToolbarTitle.text = getString(R.string.myPage_account) // 툴바 설정
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarAccount.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 프로필 공개 여부 버튼 클릭
        binding.switchAccountProfile.setOnCheckedChangeListener { _, isChecked ->
            val myPageService = MyPageService()
            myPageService.setCommonView(this)

            // 프로필 공개
            if (isChecked) {
                myPageService.showProfile(true)
            }
            // 프로필 비공개
            else {
                myPageService.showProfile(false)
            }
        }

        // 회원 탈퇴 버튼 클릭
        binding.tvAccountWithdrawal.setOnClickListener {
            // 회원 탈퇴 화면 1로 이동
            val actionToWithdrawal1: NavDirections =
                com.oops.oops_android.ui.MyPage.AccountFragmentDirections.actionAccountFrmToWithdrawal1Frm()
            view?.findNavController()?.navigate(actionToWithdrawal1)
        }
    }

    // 프로필 공개/비공개 전환 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> Log.d("AccountFragment", "Profile Success")
        }
    }

    // 프로필 공개/비공개 전환 실패
    override fun onCommonFailure(status: Int, message: String) {
        Log.d("AccountFragment", "$status $message Profile Failure")
    }
}