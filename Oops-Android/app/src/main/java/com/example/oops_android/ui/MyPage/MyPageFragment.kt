package com.example.oops_android.ui.MyPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentMyPageBinding
import com.example.oops_android.ui.Base.BaseFragment

// 마이 페이지 화면
class MyPageFragment: BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    override fun initViewCreated() {

    }

    override fun initAfterBinding() {
        // 공지형 팝업을 클릭한 경우
        binding.lLayoutMyPageCommentNotice.setOnClickListener {
            // 공지사항 화면으로 이동
            val actionToNotice: NavDirections = MyPageFragmentDirections.actionMyPageFrmToNoticeFrm()
            view?.findNavController()?.navigate(actionToNotice)
        }
    }

}