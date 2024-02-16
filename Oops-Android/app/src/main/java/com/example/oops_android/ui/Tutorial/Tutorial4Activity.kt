package com.example.oops_android.ui.Tutorial

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityTutorial4Binding
import com.example.oops_android.ui.Base.BaseActivity

// 튜토리얼4 화면(로딩 화면)
class Tutorial4Activity: BaseActivity<ActivityTutorial4Binding>(ActivityTutorial4Binding::inflate) {
    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {

        // gif 적용
        Glide.with(this)
            .load(R.raw.tutorial_gif)
            .into(binding.ivTutorial4Loading)

        // 5초 후 gif 정지
        Handler(Looper.getMainLooper()).postDelayed({
            binding.ivTutorial4Loading.setImageResource(R.drawable.tutorial_end_img)

            // 다음 버튼 띄우기
            val inAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_up)
            binding.btnTutorial4Next.visibility = View.VISIBLE
            binding.btnTutorial4Next.startAnimation(inAnim)

        }, 4700L)

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial4Next.setOnClickListener {
            // TODO: 튜토리얼5 화면으로 이동
            showToast("버튼 클릭!")
        }
    }
}