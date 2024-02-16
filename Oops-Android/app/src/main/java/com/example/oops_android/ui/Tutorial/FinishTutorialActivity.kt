package com.example.oops_android.ui.Tutorial

import android.annotation.SuppressLint
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityFinishTutorialBinding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.ui.Main.MainActivity
import com.example.oops_android.utils.getNickname

// 튜토리얼 완료 화면
class FinishTutorialActivity: BaseActivity<ActivityFinishTutorialBinding>(ActivityFinishTutorialBinding::inflate) {
    override fun beforeSetContentView() {
    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
        // 닉네임 설정
        binding.tvFinishTutorialNickname.text = getNickname() + getString(R.string.tutorial_info_13)

        // 시작하기 버튼 클릭 이벤트
        binding.btnFinishTutorial.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }
    }
}