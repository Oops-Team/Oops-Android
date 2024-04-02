package com.oops.oops_android.ui.Tutorial

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.oops.oops_android.R
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
            // EditText가 3개 이하라면
            if (addCount < 3) {
                // 할 일 edt 동적 생성하기
                val edtView = EditText(this)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 48, 0, 0)
                // 스타일 적용
                edtView.apply {
                    hint = getString(R.string.tutorial_info_11)
                    setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Gray_300)))
                    setTextColor(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.Gray_900)))
                    textSize = 15f
                    maxLines = 1
                    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20)) // 최대 20자까지 작성 가능하도록 설정
                    inputType = InputType.TYPE_CLASS_TEXT
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    setBackgroundResource(R.drawable.all_radius_12_tutorial)
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    layoutParams = params
                }
                binding.lLayoutTutorial5Todo.addView(edtView) // 레이아웃에 EditText 추가
                ++addCount

                // EditText가 3개 라면
                if (addCount == 3) {
                    // 다음 버튼 띄우기
                    binding.btnTutorial5Next.visibility = View.VISIBLE
                }
            }
        }

        // 다음 버튼 클릭 이벤트
        binding.btnTutorial5Next.setOnClickListener {
            // FinishTutorial 화면으로 이동
            startNextActivity(FinishTutorialActivity::class.java)
        }
    }
}