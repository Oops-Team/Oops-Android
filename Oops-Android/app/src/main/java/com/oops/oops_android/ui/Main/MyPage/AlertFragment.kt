package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Model.UserPushAlertChangeModel
import com.oops.oops_android.databinding.FragmentAlertBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.PushAlertAgreeDialog
import com.oops.oops_android.ui.Login.PushAlertDisagreeDialog

class AlertFragment: BaseFragment<FragmentAlertBinding>(FragmentAlertBinding::inflate), CommonView {
    private var isCancel = false // 알림 비활성화 취소 버튼을 누른 경우

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        binding.toolbarAlert.tvSubToolbarTitle.text = getString(R.string.myPage_alarm) // 툴바 설정

        // data 읽어오기
        try {
            val args: AlertFragmentArgs by navArgs()
            val isAlert = args.isAlert // 알림 설정 여부
            binding.switchAlert.isChecked = isAlert
        } catch (e: Exception) {
            Log.d("AlertFragment - Get Nav Data", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 클릭한 경우
        binding.toolbarAlert.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 토글 버튼 클릭 이벤트
        binding.switchAlert.setOnCheckedChangeListener { _, isChecked ->
            if (!isCancel && isChecked) {
                // 푸시 알림 설정 팝업 띄우기
                setPushAlert(true)
            }
            else if (!isChecked) {
                // 알림 비활성화 팝업 띄우기(한번 더 확인)
                val disableDialog = PushAlertDisableDialog(requireContext())
                disableDialog.showPushAlertDisableDialog()

                disableDialog.setOnClickedListener(object :
                    PushAlertDisableDialog.PushAlertDisableButtonClickListener {
                    override fun onClicked(isAgree: Boolean) {
                        // 비활성화 버튼을 클릭한 경우
                        if (isAgree) {
                            // 푸시 알림 설정 팝업 띄우기
                            setPushAlert(false)
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

    // 푸시 알림 설정 변경 API 연결
    private fun setPushAlert(isAlert: Boolean) {
        val myPageService = MyPageService()
        myPageService.setCommonView(this)
        myPageService.setPushAlert(UserPushAlertChangeModel(isAlert))
    }

    // 푸시 알림 설정 변경 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        // 푸시 알림을 동의했을 경우
        if (data as Boolean) {
            // 알림 동의 팝업 띄우기
            val agreeDialog = PushAlertAgreeDialog(requireContext())
            agreeDialog.showAgreeDialog()
            agreeDialog.setOnClickedListener(object : PushAlertAgreeDialog.AgreeButtonClickListener {
                override fun onClicked() {
                    // 확인 버튼을 누른 경우
                }
            })
        }
        // 푸시 알림을 비동의했을 경우
        else {
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
    }

    // 푸시 알림 설정 변경 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}