package com.oops.oops_android.ui.Tutorial

import com.oops.oops_android.R
import com.oops.oops_android.databinding.ActivityTutorial3Binding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.utils.ButtonUtils

class Tutorial3Activity: BaseActivity<ActivityTutorial3Binding>(ActivityTutorial3Binding::inflate) {

    private var isEnable = false // 다음 버튼 클릭 가능 여부
    private var previousCount = 0 // 이전에 선택했던 아이템 개수

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // 소지품 리스트 연결
        val adapter = TutorialStuffListAdapter(this)
        binding.rvTutorial3Stuff.adapter = adapter

        // 소지품 클릭 이벤트
        adapter.onStuffItemClickListener = { position ->
            adapter.setClickStuffItem(position)

            // 선택된 소지품 개수가 2 -> 3개라면
            if (previousCount == 2 && adapter.getClickedStuffList() == 3) {
                // 다음 버튼 활성화
                ButtonUtils().setColorAnimation(binding.btnTutorial3Next)
                isEnable = true
            }
            // 선택된 소지품 개수가 3개 미만이라면
            else if (adapter.getClickedStuffList() <= 2) {
                // 다음 버튼 비활성화
                binding.btnTutorial3Next.setTextAppearance(R.style.WideButtonDisableStyle)
                binding.btnTutorial3Next.setBackgroundColor(getColor(R.color.Gray_200))
                isEnable = false
            }

            // 아이템을 선택한 경우
            if (adapter.getStuffItem(position).isSelected)
                ++previousCount
            else
                --previousCount
        }

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial3Next.setOnClickListener {
            if (isEnable) {
                // 튜토리얼4 화면으로 이동
                startNextActivity(Tutorial4Activity::class.java)
            }
        }
    }
}