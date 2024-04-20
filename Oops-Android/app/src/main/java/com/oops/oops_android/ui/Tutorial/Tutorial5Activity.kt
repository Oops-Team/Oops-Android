package com.oops.oops_android.ui.Tutorial

import android.view.View
import com.oops.oops_android.databinding.ActivityTutorial5Binding
import com.oops.oops_android.ui.Base.BaseActivity

// 튜토리얼 5 화면
class Tutorial5Activity: BaseActivity<ActivityTutorial5Binding>(ActivityTutorial5Binding::inflate) {

    private var addCount = 1 // 추가한 edittext 개수

    override fun beforeSetContentView() {
    }

    override fun connectOopsAPI(token: String?, loginId: String?) {
    }

    override fun initAfterBinding() {
        // 화면 터치 시 키보드 숨기기
        binding.lLayoutTutorial5Top.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 할일 추가 버튼 클릭 이벤트
        binding.iBtnTutorial5TodoAdd.setOnClickListener {
            // 1개라면
            if (addCount == 1) {
                binding.tvTutorial5Todo1.text = "민정이랑 전주 여행"
                binding.tvTutorial5Todo2.visibility = View.VISIBLE
                ++addCount
            }
            // 2개라면
            else if (addCount == 2) {
                binding.tvTutorial5Todo2.text = "비행기 표 예매"
                binding.tvTutorial5Todo3.visibility = View.VISIBLE

                // 다음 버튼 띄우기
                binding.btnTutorial5Next.visibility = View.VISIBLE
                ++addCount
            }
        }

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial5Next.setOnClickListener {
            // FinishTutorial 화면으로 이동
            startNextActivity(FinishTutorialActivity::class.java)
        }
    }
}