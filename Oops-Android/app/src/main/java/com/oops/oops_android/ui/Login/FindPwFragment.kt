package com.oops.oops_android.ui.Login

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Auth.Api.AuthService
import com.oops.oops_android.data.remote.Auth.Model.FindOopsPwModel
import com.oops.oops_android.data.remote.Common.CommonObjectView
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.databinding.FragmentFindPwBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.onTextChanged
import org.json.JSONObject
import java.util.*

/* PW 찾기 및 PW 재설정 화면 */
class FindPwFragment: BaseFragment<FragmentFindPwBinding>(FragmentFindPwBinding::inflate), CommonView, CommonObjectView {

    private var time = 0
    private var timerTask: TimerTask? = null
    private var tempToken: String? = null
    private var isBtnEnable: Boolean = false // 비밀번호 재설정 버튼 활성화 여부

    override fun initViewCreated() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerTask?.cancel() // timerTask 멈추기
    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
        // 이메일 edt 입력 이벤트
        binding.edtFindPwEmail.onTextChanged {
            binding.tvFindPwCodeBtn.isEnabled = true // 코드 전송 버튼 실행
            binding.tvFindPwEmailAlert.visibility = View.INVISIBLE // alert 삭제
            binding.viewFindPwEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray_300))
        }

        // 코드 전송 버튼 클릭 이벤트
        binding.tvFindPwCodeBtn.setOnClickListener {
            // 이메일 형식이 맞다면
            if (EditTextUtils.emailRegex(binding.edtFindPwEmail.text.toString().trim())) {
                binding.tvFindPwCodeTimer.text = "02:00"
                binding.viewFindPwCode.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray_300))
                binding.tvFindPwCodeCertifyAlert.visibility = View.INVISIBLE

                // progressBar 띄우기
                binding.lLayoutFindPwProgressBar.visibility = View.VISIBLE

                // 비밀번호 찾기 - 코드 전송 API 연결
                binding.tvFindPwCodeBtn.isEnabled = false // 코드 전송 버튼 비활성화
                binding.edtFindPwEmail.isEnabled = false // 이메일 입력 비활성화

                val authService = AuthService()
                authService.setCommonView(this)
                authService.findOopsPwCode(binding.edtFindPwEmail.text.toString())
            }
            else {
                // 오류 문구 띄우기
                binding.tvFindPwEmailAlert.visibility = View.VISIBLE
                binding.tvFindPwEmailAlert.text = getString(R.string.login_email_alert)
                binding.tvFindPwEmailAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                binding.viewFindPwEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Dark))
            }
        }

        // 인증하기 버튼 클릭 이벤트
        binding.tvFindPwCodeCertifyBtn.setOnClickListener {
            val authService = AuthService()
            authService.setCommonObjectView(this)
            authService.findOopsPw(FindOopsPwModel(binding.edtFindPwCode.text.toString(), binding.edtFindPwEmail.text.toString()))
        }

        // 비밀번호 재설정 버튼 클릭 이벤트
        binding.btnFindIdPwReset.setOnClickListener {
            if (isBtnEnable) {
                // 비밀번호 재설정 뷰로 바꾸기
                binding.lLayoutFindPwCode.visibility = View.GONE
                binding.lLayoutFindPwNewPw.visibility = View.VISIBLE
            }
        }
    }

    // 비밀번호 찾기 - 코드 전송 API 연결 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                binding.lLayoutFindPwProgressBar.visibility = View.GONE // progressbar 숨기기
                binding.edtFindPwCode.isEnabled = true // 인증번호 입력 활성화

                binding.tvFindPwEmailAlert.visibility = View.VISIBLE
                binding.tvFindPwEmailAlert.text = getString(R.string.find_pw_code_info_1)
                binding.tvFindPwEmailAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Main_400))
                binding.viewFindPwEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_400))

                binding.tvFindPwCodeCertifyBtn.isEnabled = true // 인증하기 버튼 활성화

                // 2분 타이머 시작
                start2MTimer()
            }
        }
    }

    // 2분 타이머 작동
    @SuppressLint("SetTextI18n")
    private fun start2MTimer() {
        binding.tvFindPwCodeTimer.visibility = View.VISIBLE
        binding.tvFindPwCodeTimer.text = "02:00"
        time = 120 // 제한시간 2분

        timerTask = object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    // 0초가 되었을 경우
                    if (time <= 0) {
                        timerTask?.cancel()
                        binding.tvFindPwCodeCertifyAlert.visibility = View.VISIBLE
                        binding.tvFindPwCodeCertifyAlert.text = resources.getString(R.string.find_pw_code_info_4)
                        binding.tvFindPwCodeCertifyAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                        binding.viewFindPwCode.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                        binding.viewFindPwEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray_300))
                        binding.tvFindPwEmailAlert.visibility = View.INVISIBLE

                        // 이메일 입력 및 코드 전송 버튼 클릭 활성화
                        binding.edtFindPwEmail.isEnabled = true
                        binding.tvFindPwCodeBtn.isEnabled = true

                        // 인증하기 버튼 비활성화
                        binding.tvFindPwCodeCertifyBtn.isEnabled = false
                    }

                    time -= 1
                    val min = time / 60
                    val sec = time % 60
                    binding.tvFindPwCodeTimer.text = String.format("%02d:%02d", min, sec)
                }
            }
        }
        val timer = Timer()
        timer.schedule(timerTask, 0, 1000)
    }

    // 비밀번호 찾기 - 코드 전송 API 연결 실패
    override fun onCommonFailure(status: Int, message: String) {
        when (status) {
            404 -> {
                // 등록된 이메일이 없는 경우
                binding.tvFindPwCodeBtn.isEnabled = true // 코드 전송 버튼 활성화
                binding.edtFindPwEmail.isEnabled = true // 이메일 입력 활성화
                binding.tvFindPwEmailAlert.visibility = View.VISIBLE
                binding.tvFindPwEmailAlert.text = getString(R.string.find_id_email_info_1)
                binding.tvFindPwEmailAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                binding.viewFindPwEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
            }
            else -> showToast(getString(R.string.toast_server_error))
        }
    }

    // 비밀번호 찾기 - 코드 인증 API 연결 성공
    override fun onCommonObjectSuccess(status: Int, message: String, data: JsonObject?) {
        when (status) {
            200 -> {
                // timerTask 멈추기
                timerTask?.cancel()
                binding.tvFindPwCodeTimer.visibility = View.INVISIBLE

                // 토큰 파싱
                val jsonObject = JSONObject(data.toString())
                tempToken = jsonObject.getString("tempToken").toString() // 비밀번호 재설정을 위한 임시 토큰

                binding.tvFindPwCodeBtn.isEnabled = false
                binding.edtFindPwCode.isEnabled = false
                binding.tvFindPwCodeCertifyAlert.visibility = View.VISIBLE
                binding.tvFindPwCodeCertifyAlert.text = resources.getString(R.string.find_pw_code_info_3)
                binding.tvFindPwCodeCertifyAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Main_400))
                binding.viewFindPwCode.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_400))

                // 버튼 활성화
                isBtnEnable = true
                binding.btnFindIdPwReset.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Main_500))
                binding.btnFindIdPwReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.White))
            }
        }
    }

    // 비밀번호 찾기 - 코드 인증 API 연결 실패
    override fun onCommonObjectFailure(status: Int, message: String) {
        isBtnEnable = false // 버튼 비활성화
        binding.btnFindIdPwReset.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Gray_100))
        binding.btnFindIdPwReset.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray_400))

        when (status) {
            400 -> {
                // 인증코드가 일치하지 않는 경우
                binding.tvFindPwCodeCertifyAlert.visibility = View.VISIBLE
                binding.tvFindPwCodeCertifyAlert.text = resources.getString(R.string.find_pw_code_info_2)
                binding.tvFindPwCodeCertifyAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                binding.viewFindPwCode.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
            }
            401, 404 -> showToast(message)
            else -> showToast(getString(R.string.toast_server_error))
        }
    }
}