package com.oops.oops_android.ui.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Auth.Api.AuthService
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.databinding.FragmentFindIdBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.EditTextUtils
import com.oops.oops_android.utils.onTextChanged
import com.oops.oops_android.utils.setOnSingleClickListener
import org.json.JSONObject

/* ID 찾기 화면 */
class FindIdFragment: BaseFragment<FragmentFindIdBinding>(FragmentFindIdBinding::inflate), CommonView {
    override fun initViewCreated() {
    }

    override fun initAfterBinding() {
        // 이메일 edt 입력 이벤트
        binding.edtFindIdEmail.onTextChanged {
            binding.tvFindIdEmailBtn.isEnabled = true // 조회하기 버튼 실행
            binding.lLayoutFindIdBtnLayout.visibility = View.GONE // 버튼 레이아웃 숨기기
            binding.tvFindIdAlert.visibility = View.GONE // alert 삭제
            binding.viewFindIdEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray_300))
        }

        // 이메일 찾기 버튼 클릭 이벤트
        binding.tvFindIdEmailBtn.setOnSingleClickListener {
            // 이메일 형식이 맞다면
            if (EditTextUtils.emailRegex(binding.edtFindIdEmail.text.toString().trim())) {
                // 이메일 찾기 API 연결
                val authService = AuthService()
                authService.setCommonView(this)
                authService.findOopsEmail(binding.edtFindIdEmail.text.toString())
            }
            else {
                // 오류 문구 띄우기
                binding.tvFindIdAlert.visibility = View.VISIBLE
                binding.tvFindIdAlert.text = getString(R.string.login_email_alert)
                binding.tvFindIdAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                binding.viewFindIdEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Dark))
            }
        }

        // 비밀번호 재설정 버튼 클릭 이벤트
        binding.btnFindIdPwReset.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp_find_account)
            viewPager.currentItem = 1
        }

        // 로그인하러가기 버튼 클릭 이벤트
        binding.btnFindIdGoLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    // 이메일 찾기 성공
    @SuppressLint("SetTextI18n")
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                binding.tvFindIdAlert.visibility = View.VISIBLE
                binding.tvFindIdAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
                binding.viewFindIdEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))

                // 조회하기 버튼 막기
                binding.tvFindIdEmailBtn.isEnabled = false

                // oops로 가입했다면
                if (data == null) {
                    binding.tvFindIdAlert.text = getString(R.string.find_id_email_info_3)

                    // btn 레이아웃 띄우기(애니메이션 적용)
                    val inAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_slide_in_up)
                    binding.lLayoutFindIdBtnLayout.visibility = View.VISIBLE
                    binding.lLayoutFindIdBtnLayout.startAnimation(inAnim)
                }
                // naver 또는 구글로 가입했다면
                else {
                    binding.tvFindIdAlert.text = JSONObject(data.toString()).toString() + getString(R.string.find_id_email_info_2)
                }
            }
        }
    }

    // 이메일 찾기 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            404 -> {
                // 등록된 이메일이 없는 경우
                binding.tvFindIdAlert.visibility = View.VISIBLE
                binding.tvFindIdAlert.text = getString(R.string.find_id_email_info_1)
                binding.tvFindIdAlert.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
                binding.viewFindIdEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Red_Medium))
            }
            else -> {
                showToast(getString(R.string.toast_server_error))
            }
        }
    }
}