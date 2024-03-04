package com.oops.oops_android.ui.Login

import android.text.InputFilter
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Auth.AuthService
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.databinding.ActivitySignUpBinding
import com.oops.oops_android.ui.Base.BaseActivity
import com.oops.oops_android.ui.Main.MainActivity
import com.oops.oops_android.utils.getLoginId
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.saveNickname
import java.util.regex.Pattern

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate), CommonView {
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
                alert.text = getString(R.string.signup_nickname_alert_1)
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
                // 닉네임 중복 검사하기
                nicknameOverlap(binding.edtSignUpNickname.text.toString())
            }

            getHideKeyboard(binding.root) // 키보드 숨기기
        }

        /** 닉네임 edt 입력 이벤트 **/
        binding.edtSignUpNickname.onTextChanged {
            // 다음 버튼 숨기기
            binding.cvSignUp1Next.visibility = View.INVISIBLE

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

            // 사용 버튼을 클릭한 경우
            dialog.setOnClickedListener(object : NicknameDialog.ButtonClickListener {
                override fun onClicked() {
                    // 닉네임 저장
                    saveNickname(binding.edtSignUpNickname.text.toString())

                    // 네이버 로그인하기의 경우
                    if (getLoginId() == "naver") {
                        // 개인정보 수집 및 이용 동의 바텀 시트 띄우기
                        val termsBottomSheet = TermsBottomSheetFragment { item, isChoiceCheck ->
                            when (item) {
                                0 -> startNextActivity(MoreTermsActivity::class.java) // 개인정보 이용약관 보기 버튼
                                1 -> clickNextBtn(isChoiceCheck) // 다음 버튼
                            }
                        }
                        termsBottomSheet.show(supportFragmentManager, termsBottomSheet.tag)
                    }
                    else {
                        // 이메일&비밀번호 입력 화면으로 이동
                        startActivityWithClear(SignUp2Activity::class.java)
                    }
                }
            })
        }
    }

    // 다음 버튼 클릭 이벤트
    private fun clickNextBtn(isChoiceCheck: Boolean) {
        // 닉네임 저장
        saveNickname(binding.edtSignUpNickname.text.toString())

        // 알림에 동의했다면
        if (isChoiceCheck) {
            clickAgreeBtn()
        }
        // 알림에 미동의했다면
        else {
            // 푸시 알림 미동의 팝업 띄우기
            val dialog = PushAlertDialog(this@SignUpActivity)
            dialog.showPushAlertDialog()
            dialog.setOnClickedListener(object : PushAlertDialog.PushAlertButtonClickListener {
                override fun onClicked(isAgree: Boolean) {
                    // 동의 버튼을 누른 경우
                    if (isAgree)
                        clickAgreeBtn()
                    // 동의 안함 버튼을 누른 경우
                    else
                        clickDisAgreeBtn()
                }
            })
        }
    }

    // 푸시 알림 동의 버튼을 누른 경우
    private fun clickAgreeBtn() {
        val agreeDialog = PushAlertAgreeDialog(this@SignUpActivity)
        agreeDialog.showAgreeDialog()
        agreeDialog.setOnClickedListener(object : PushAlertAgreeDialog.AgreeButtonClickListener {
            override fun onClicked() {
                // 확인 버튼을 누른 경우
                startActivityWithClear(MainActivity::class.java)
            }
        })
    }

    // 푸시 알림 동의 안함 버튼을 누른 경우
    private fun clickDisAgreeBtn() {
        val disagreeDialog = PushAlertDisagreeDialog(this@SignUpActivity)
        disagreeDialog.showDisagreeDialog()
        disagreeDialog.setOnClickedListener(object : PushAlertDisagreeDialog.DisAgreeButtonClickListener {
            override fun onClicked() {
                // 확인 버튼을 누른 경우
                startActivityWithClear(MainActivity::class.java)
            }
        })
    }

    // 서버 닉네임 체크
    private fun nicknameOverlap(name: String) {
        val authService = AuthService()
        authService.setCommonView(this)
        authService.nicknameOverlap(name)
    }

    // 닉네임 중복 검사 성공
    override fun onCommonSuccess(status: Int, message: String) {
        when (status) {
            200 -> {
                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE

                val alert = binding.tvSignUpAlert
                val alertImg = binding.ivSignUpAlert

                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_confirm)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                binding.viewSignUpNickname.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Main_500))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_confirm_25)

                // 다음 버튼 띄우기
                // 등장 애니메이션 적용
                val inAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_up)
                binding.cvSignUp1Next.visibility = View.VISIBLE
                binding.cvSignUp1Next.startAnimation(inAnim)
            }
        }
    }

    // 닉네임 중복 검사 실패
    override fun onCommonFailure(status: Int, message: String) {
        when (status) {
            // 닉네임 중복인 경우
            409 -> {
                // 중복 확인 버튼 숨기기
                binding.tvSignUpOverlapBtn.visibility = View.INVISIBLE

                val alert = binding.tvSignUpAlert
                val underLine = binding.viewSignUpNickname
                val alertImg = binding.ivSignUpAlert

                alert.visibility = View.VISIBLE
                alert.text = getString(R.string.signup_nickname_alert_2)
                alert.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red_Medium))
                underLine.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.Red_Dark))
                alertImg.visibility = View.VISIBLE
                alertImg.setImageResource(R.drawable.ic_mark_25)
            }
            else -> {
                showToast(getString(R.string.toast_server_error))
            }
        }
    }
}