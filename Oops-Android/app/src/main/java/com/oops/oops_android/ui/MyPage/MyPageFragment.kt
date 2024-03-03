package com.oops.oops_android.ui.MyPage

import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.databinding.FragmentMyPageBinding
import com.oops.oops_android.ui.Base.BaseFragment

// 마이 페이지 화면
class MyPageFragment: BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    override fun initViewCreated() {
        mainActivity?.hideBnv(false)
    }

    override fun initAfterBinding() {
        // 공지형 팝업을 클릭한 경우
        binding.lLayoutMyPageCommentNotice.setOnClickListener {
            // 공지사항 화면으로 이동
            val actionToNotice: NavDirections = MyPageFragmentDirections.actionMyPageFrmToNoticeFrm()
            view?.findNavController()?.navigate(actionToNotice)
        }

        // 계정 설정 탭을 클릭한 경우
        binding.lLayoutMyPageAccount.setOnClickListener {
            val actionToAccount: NavDirections = MyPageFragmentDirections.actionMyPageFrmToAccountFrm()
            view?.findNavController()?.navigate(actionToAccount)
        }
        // 알림 설정 탭을 클릭한 경우
        binding.lLayoutMyPageAlarm.setOnClickListener {
            val actionToAlert: NavDirections = MyPageFragmentDirections.actionMyPageFrmToAlertFrm()
            view?.findNavController()?.navigate(actionToAlert)
        }
    }

}