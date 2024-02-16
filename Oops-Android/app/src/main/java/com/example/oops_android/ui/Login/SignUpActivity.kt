package com.example.oops_android.ui.Login

import android.content.res.ColorStateList
import android.text.InputFilter
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.ActivitySignUpBinding
import com.example.oops_android.ui.Base.BaseActivity
import com.example.oops_android.utils.onTextChanged
import com.example.oops_android.utils.saveNickname
import java.util.regex.Pattern

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {
    override fun beforeSetContentView() {
    }

    override fun initAfterBinding() {

        // 화면 터치 시 키보드 숨기기
        binding.cLayoutSignUpTop.setOnClickListener {
            getHideKeyboard(binding.root)
        }

        // 한글, 영어 대소문자, 숫자만 입력 가능하도록 필터 적용
        binding.edtSignUpNickname.filters = arrayOf(
            InputFilter { source, start, end, dest, dstart, dend ->
                val ps = Pattern.compile(".*[a-zA-Z0-9ㄱ-ㅎ가-힣- ]+.*")
                if (!ps.matcher(source).matches()) {
                    return@InputFilter ""
                } else{
                    return@InputFilter null
                }
            }
        )

        // 중복 확인 버튼 클릭 이벤트
        binding.tvSignUpOverlapBtn.setOnClickListener {
            val alert = binding.tvSignUpAlert
            val edt = binding.edtSignUpNickname
            val underLine = binding.viewSignUpNickname
            val alertImg = binding.ivSignUpAlert

            // 7자 이상 입력했다면
            if (edt.text.length >= 7) {
                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                underLine.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_mark_25)

                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE
            }
            // 이모지, 특수 문자가 입력됐다면
            /*else if () {
            }*/
            // 6자 이하 입력 및 조건에 맞다면
            else if (edt.text.isNotEmpty()){
                checkNickName(alert, edt, alertImg)
            }

            getHideKeyboard(binding.root) // 키보드 숨기기
        }

        /** 닉네임 edt 입력 이벤트 **/
        binding.edtSignUpNickname.onTextChanged {
            // 다음 버튼 숨기기
            binding.btnSignUp1Next.visibility = View.INVISIBLE

            // 10자 미만 입력됐다면
            if (binding.edtSignUpNickname.length() in 1..6) {
                binding.tvSignUpOverlapBtn.visibility = View.VISIBLE // 중복 확인 버튼 나타내기
                binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Gray_300))
                binding.ivSignUpAlert.visibility = View.GONE // 알럿 제거
                binding.tvSignUpAlert.visibility = View.GONE // alert 제거
            }
        }

        // 다음 버튼 클릭 이벤트
        binding.btnSignUp1Next.setOnClickListener {
            // 닉네임 변경 불가 팝업 띄우기
            val dialog = NicknameDialog(this@SignUpActivity)
            dialog.nicknameDialogShow()

            dialog.setOnClickedListener(object : NicknameDialog.ButtonClickListener {
                override fun onClicked() {
                    // 사용 버튼을 클릭한 경우
                    // 닉네임 저장
                    saveNickname(binding.edtSignUpNickname.text.toString())
                    startActivityWithClear(SignUp2Activity::class.java)
                }
            })
        }
    }

    // 닉네임을 잘 입력했는지 체크
    private fun checkNickName(alert: TextView, edt: EditText, alertImg: ImageView) {

        // TODO: 서버api 연동 -> 중복 닉네임 체크
        // ok일 경우 -> 버튼 나타내기


        // 조건에 만족한 경우
        // 중복 확인 버튼 숨기기
        binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE

        alert.visibility = View.VISIBLE
        alert.text = getString(R.string.signup_nickname_alert_confirm)
        alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
        binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
        alertImg.visibility = View.VISIBLE
        alertImg.setImageResource(R.drawable.ic_confirm_25)
        //val confirmBtn = ContextCompat.getDrawable(applicationContext, R.drawable.ic_confirm_25)
        //edt.setCompoundDrawablesWithIntrinsicBounds(null, null, confirmBtn, null)

        // 다음 버튼 띄우기
        // 등장 애니메이션 적용
        val inAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_up)
        binding.btnSignUp1Next.visibility = View.VISIBLE
        binding.btnSignUp1Next.startAnimation(inAnim)
    }
}