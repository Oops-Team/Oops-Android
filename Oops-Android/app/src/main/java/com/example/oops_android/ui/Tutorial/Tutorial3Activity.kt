package com.example.oops_android.ui.Tutorial

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityTutorial3Binding
import com.example.oops_android.ui.Base.BaseActivity

class Tutorial3Activity: BaseActivity<ActivityTutorial3Binding>(ActivityTutorial3Binding::inflate) {

    private var isEnable = false // 다음 버튼 클릭 가능 여부

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // 상단 상태바 설정
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.Gray_50)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        
        // 소지품 리스트 연결
        val adapter = TutorialStuffListAdapter(this)
        binding.rvTutorial3Stuff.adapter = adapter

        // 소지품 클릭 이벤트
        adapter.onStuffItemClickListener = { position ->
            adapter.setClickStuffItem(position)

            // 선택된 소지품 개수가 3개 이상이라면
            isEnable =
                if (adapter.getClickedStuffList() >= 3) {
                    // 다음 버튼 활성화
                    binding.btnTutorial3Next.setTextAppearance(R.style.WideButtonEnableStyle)
                    binding.btnTutorial3Next.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Main_500))
                    true
                } else {
                    // 다음 버튼 비활성화
                    binding.btnTutorial3Next.setTextAppearance(R.style.WideButtonDisableStyle)
                    binding.btnTutorial3Next.backgroundTintList = ColorStateList.valueOf(getColor(R.color.Gray_200))
                    false
                }
        }

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial3Next.setOnClickListener {
            if (isEnable) {
                // TODO: 튜토리얼4 화면으로 이동
                showToast("다음 버튼 클릭!")
            }
        }
    }
}