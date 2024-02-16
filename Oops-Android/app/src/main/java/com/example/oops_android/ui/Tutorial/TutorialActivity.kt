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
import com.example.oops_android.ui.Main.MainActivity
import com.example.oops_android.utils.getNickname

// 튜토리얼 화면 - 1
class TutorialActivity: BaseActivity<ActivityTutorialBinding>(ActivityTutorialBinding::inflate) {
    override fun beforeSetContentView() {
    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
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

        // 튜토리얼 2 이동 버튼 클릭 이벤트
        binding.btnTutorialWonder.setOnClickListener {
            startNextActivity(Tutorial2Activity::class.java)
        }

        // 홈 화면 이동 버튼 클릭 이벤트
        binding.btnTutorialStart.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.White) // 상단 상태바 색상 변경
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }
}