package com.oops.oops_android.ui.MyPage

import androidx.navigation.findNavController
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentAlertBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.PushAlertAgreeDialog
import com.oops.oops_android.ui.Login.PushAlertDisagreeDialog

class AlertFragment: BaseFragment<FragmentAlertBinding>(FragmentAlertBinding::inflate) {
    private var isCancel = false // 알림 비활성화 취소 버튼을 누른 경우

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        binding.toolbarAlert.tvSubToolbarTitle.text = getString(R.string.myPage_alarm) // 툴바 설정
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 클릭한 경우
        binding.toolbarAlert.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 토글 버튼 클릭 이벤트
        binding.switchAlert.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isCancel && isChecked) {
                // 알림 동의 팝업 띄우기
                val agreeDialog = PushAlertAgreeDialog(requireContext())
                agreeDialog.showAgreeDialog()
                agreeDialog.setOnClickedListener(object : PushAlertAgreeDialog.AgreeButtonClickListener {
                    override fun onClicked() {
                        // 확인 버튼을 누른 경우
                    }
                })
            }
            else if (!isChecked) {
                // 알림 비활성화 팝업 띄우기(한번 더 확인)
                val disableDialog = PushAlertDisableDialog(requireContext())
                disableDialog.showPushAlertDisableDialog()

                disableDialog.setOnClickedListener(object : PushAlertDisableDialog.PushAlertDisableButtonClickListener {
                    override fun onClicked(isAgree: Boolean) {
                        // 비활성화 버튼을 클릭한 경우
                        if (isAgree) {
                            // 알림 비동의 팝업 띄우기
                            val disagreeDialog = PushAlertDisagreeDialog(requireContext())
                            disagreeDialog.showDisagreeDialog()
                            disagreeDialog.setOnClickedListener(object : PushAlertDisagreeDialog.DisAgreeButtonClickListener {
                                override fun onClicked() {
                                    // 확인 버튼을 누른 경우
                                    isCancel = false
                                }
                            })
                        }
                        // 취소 버튼을 클릭한 경우
                        else {
                            isCancel = true
                            binding.switchAlert.isChecked = true // 알림 유지
                        }
                    }
                })
            }
        }
    }
}