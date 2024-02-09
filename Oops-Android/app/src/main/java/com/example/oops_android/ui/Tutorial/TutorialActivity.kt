package com.example.oops_android.ui.Tutorial

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityTutorialBinding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.utils.getNickname

// 튜토리얼 화면 - 1
class TutorialActivity: BaseActivity<ActivityTutorialBinding>(ActivityTutorialBinding::inflate) {
    override fun beforeSetContentView() {
    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
        /* 상태바 숨기기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }*/

        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.Main_500) // 상단 상태바 색상
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // 일부 텍스트 색상 지정
        val spannableBuilder1 = SpannableStringBuilder(binding.tvTutorialOops.text.toString())
        val colorWhite = ForegroundColorSpan(getColor(R.color.White))
        spannableBuilder1.setSpan(colorWhite, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTutorialOops.text = spannableBuilder1

        binding.tvTutorialNickname.text = getNickname() + " " + getString(R.string.tutorial_info_2)
        val spannableBuilder2 = SpannableStringBuilder(binding.tvTutorialNickname.text.toString())
        spannableBuilder2.setSpan(colorWhite, 0, getNickname().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTutorialNickname.text = spannableBuilder2

        // 배경색 변경 및 이미지 이동 애니메이션 적용
        val intervalBgAnimation = AnimatorInflater.loadAnimator(this, R.animator.object_animator_tutorial)
        intervalBgAnimation.setTarget(binding.lLayoutTutorialTop)
        val intervalYAnimation = AnimatorInflater.loadAnimator(this, R.animator.object_animator_mark)
        intervalYAnimation.setTarget(binding.ivQuestionMark)

        intervalBgAnimation.start()
        intervalYAnimation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.White) // 상단 상태바 색상 변경
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }
}