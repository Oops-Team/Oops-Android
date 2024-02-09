package com.example.oops_android.ui.Tutorial

import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivityTutorial2Binding
import com.example.oops_android.ui.Base.BaseActivity

class Tutorial2Activity: BaseActivity<ActivityTutorial2Binding>(ActivityTutorial2Binding::inflate) {

    private var isClickView: Boolean = false

    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {
        // 상단 상태바 설정
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.Gray_50)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        // 여행 버튼 클릭
        binding.fLayoutTutorial2Travel.setOnClickListener {
            setClickTagBg(binding.fLayoutTutorial2Travel, binding.tvTutorial2Travel, binding.ivTutorial2Travel)
            setOtherTagBg(
                binding.fLayoutTutorial2Daily, binding.tvTutorial2Daily, binding.ivTutorial2Daily,
                binding.fLayoutTutorial2Study, binding.tvTutorial2Study, binding.ivTutorial2Study,
                binding.fLayoutTutorial2Exercise, binding.tvTutorial2Exercise, binding.ivTutorial2Exercise
            )
            isClickView = true
            updateNextBtnBg()
        }

        // 일상 버튼 클릭
        binding.fLayoutTutorial2Daily.setOnClickListener {
            setClickTagBg(binding.fLayoutTutorial2Daily, binding.tvTutorial2Daily, binding.ivTutorial2Daily)
            setOtherTagBg(
                binding.fLayoutTutorial2Travel, binding.tvTutorial2Travel, binding.ivTutorial2Travel,
                binding.fLayoutTutorial2Study, binding.tvTutorial2Study, binding.ivTutorial2Study,
                binding.fLayoutTutorial2Exercise, binding.tvTutorial2Exercise, binding.ivTutorial2Exercise
            )
            isClickView = true
            updateNextBtnBg()
        }

        // 수업 버튼 클릭
        binding.fLayoutTutorial2Study.setOnClickListener {
            setClickTagBg(binding.fLayoutTutorial2Study, binding.tvTutorial2Study, binding.ivTutorial2Study)
            setOtherTagBg(
                binding.fLayoutTutorial2Travel, binding.tvTutorial2Travel, binding.ivTutorial2Travel,
                binding.fLayoutTutorial2Daily, binding.tvTutorial2Daily, binding.ivTutorial2Daily,
                binding.fLayoutTutorial2Exercise, binding.tvTutorial2Exercise, binding.ivTutorial2Exercise
            )
            isClickView = true
            updateNextBtnBg()
        }

        // 운동 버튼 클릭
        binding.fLayoutTutorial2Exercise.setOnClickListener {
            setClickTagBg(binding.fLayoutTutorial2Exercise, binding.tvTutorial2Exercise, binding.ivTutorial2Exercise)
            setOtherTagBg(
                binding.fLayoutTutorial2Travel, binding.tvTutorial2Travel, binding.ivTutorial2Travel,
                binding.fLayoutTutorial2Daily, binding.tvTutorial2Daily, binding.ivTutorial2Daily,
                binding.fLayoutTutorial2Study, binding.tvTutorial2Study, binding.ivTutorial2Study
            )
            isClickView = true
            updateNextBtnBg()
        }

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial2Next.setOnClickListener {
            if (isClickView) {
                // TODO: 튜토리얼 3(소지품 선택) 화면으로 이동
                showToast("다음 버튼 클릭!")
            }
        }
    }

    // 다음 버튼 디자인 업데이트
    private fun updateNextBtnBg() {
        binding.btnTutorial2Next.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.Main_500))
            setTextColor(ColorStateList.valueOf(getColor(R.color.White)))
        }
    }

    // 태그 클릭 이벤트 함수
    private fun setClickTagBg(view: FrameLayout, tv: TextView, iv: ImageView) {
        view.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
        tv.setTextColor(ColorStateList.valueOf(getColor(R.color.White)))

        // 이미지를 흑백으로 변경
        val matrix = ColorMatrix()
        matrix.setSaturation(0F)
        val colorFilter = ColorMatrixColorFilter(matrix)
        iv.colorFilter = colorFilter
    }

    // 선택 안 된 태그들 디자인 처리 함수
    private fun setOtherTagBg(
        view1: FrameLayout, tv1: TextView, iv1: ImageView,
        view2: FrameLayout, tv2: TextView, iv2: ImageView,
        view3: FrameLayout, tv3: TextView, iv3: ImageView
    ) {
        view1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.White))
        view2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.White))
        view3.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.White))

        tv1.setTextColor(ColorStateList.valueOf(getColor(R.color.Black)))
        tv2.setTextColor(ColorStateList.valueOf(getColor(R.color.Black)))
        tv3.setTextColor(ColorStateList.valueOf(getColor(R.color.Black)))

        // 필터를 원래대로 설정
        iv1.colorFilter = null
        iv2.colorFilter = null
        iv3.colorFilter = null
    }
}