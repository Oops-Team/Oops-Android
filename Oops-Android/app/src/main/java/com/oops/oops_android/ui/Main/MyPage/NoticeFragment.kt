package com.oops.oops_android.ui.Main.MyPage

import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.databinding.FragmentNoticeBinding
import com.oops.oops_android.ui.Base.BaseFragment

class NoticeFragment: BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate) {
    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        // 툴 바 제목 설정
        binding.toolbarNotice.tvSubToolbarTitle.text = "공지사항"
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarNotice.ivSubToolbarBack.setOnClickListener {
            // 마이페이지 화면으로 이동
            view?.findNavController()?.popBackStack()
        }

        // 공지사항1 클릭
        binding.lLayoutNotice1.setOnClickListener {
            // 세부 화면으로 이동
            val actionToNoticeDetail: NavDirections =
                com.oops.oops_android.ui.MyPage.NoticeFragmentDirections.actionNoticeFrmToNoticeDetailFrm()
            view?.findNavController()?.navigate(actionToNoticeDetail)
        }
    }

}