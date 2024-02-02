package com.example.oops_android.ui.MyPage

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentAlertBinding
import com.example.oops_android.ui.Base.BaseFragment

class AlertFragment: BaseFragment<FragmentAlertBinding>(FragmentAlertBinding::inflate) {
    override fun initViewCreated() {
        binding.toolbarAlert.tvSubToolbarTitle.text = getString(R.string.myPage_alarm) // 툴바 설정
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 클릭한 경우
        binding.toolbarAlert.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}