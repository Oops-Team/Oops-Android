package com.example.oops_android.ui.MyPage

import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentNoticeDetailBinding
import com.example.oops_android.ui.Base.BaseFragment

class NoticeDetailFragment: BaseFragment<FragmentNoticeDetailBinding>(FragmentNoticeDetailBinding::inflate) {
    override fun initViewCreated() {
        // 툴 바 제목 설정
        binding.toolbarNoticeDetail.tvSubToolbarTitle.text = "공지사항"
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarNoticeDetail.ivSubToolbarBack.setOnClickListener {
            // 공지사항 화면으로 이동
            view?.findNavController()?.popBackStack()
        }
    }
}