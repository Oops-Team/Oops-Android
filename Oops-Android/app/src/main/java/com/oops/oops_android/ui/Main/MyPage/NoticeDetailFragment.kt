package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.databinding.FragmentNoticeDetailBinding
import com.oops.oops_android.ui.Base.BaseFragment

/* 상세 공지사항 화면 */
class NoticeDetailFragment: BaseFragment<FragmentNoticeDetailBinding>(FragmentNoticeDetailBinding::inflate) {
    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        // 툴 바 제목 설정
        binding.toolbarNoticeDetail.tvSubToolbarTitle.text = "공지사항"

        // nav data 가져오기
        try {
            val navArgs: NoticeDetailFragmentArgs by navArgs()
            val noticeItem = navArgs.noticeItem

            // data 적용하기
            binding.tvNoticeDetailTitle.text = noticeItem?.noticeTitle
            binding.tvNoticeDetailDate.text = noticeItem?.date
            binding.tvNoticeDetailContent.text = noticeItem?.content
        }
        catch (e: Exception) {
            Log.d("NoticeDetailFragment - Get Nav Data", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarNoticeDetail.ivSubToolbarBack.setOnClickListener {
            // 공지사항 화면으로 이동
            view?.findNavController()?.popBackStack()
        }
    }
}