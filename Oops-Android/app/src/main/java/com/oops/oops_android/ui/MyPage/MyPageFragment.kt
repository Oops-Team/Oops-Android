package com.oops.oops_android.ui.MyPage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.databinding.FragmentMyPageBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.clearToken

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

        // 로그아웃을 클릭한 경우
        binding.tvMyPageLogout.setOnClickListener {
            clearToken()

            // 로그인 화면으로 이동
            showToast("로그아웃했습니다")
            requireActivity().let {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

}