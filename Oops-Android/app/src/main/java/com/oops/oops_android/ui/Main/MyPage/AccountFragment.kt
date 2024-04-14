package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Model.UserShowProfileChangeModel
import com.oops.oops_android.databinding.FragmentAccountBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity

// 계정 관리 화면
class AccountFragment: BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate), CommonView {
    // 사용자 정보
    private lateinit var myPageItem: MyPageItem

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        binding.toolbarAccount.tvSubToolbarTitle.text = getString(R.string.myPage_account) // 툴바 설정

        // nav data 읽어오기
        try {
            val args: AccountFragmentArgs by navArgs()
            myPageItem = args.myPageItem!!

            // data 적용
            binding.tvAccountNickname.text = myPageItem.userName
            binding.tvAccountEmail.text = myPageItem.userEmail
            binding.switchAccountProfile.isChecked = myPageItem.isPublic

            when (myPageItem.loginType) {
                "google" -> {
                    binding.iBtnAccountGoogle.setImageResource(R.drawable.ic_login_google_18)
                    binding.iBtnAccountNaver.setImageResource(R.drawable.ic_login_naver_unselected_18)
                }
                "naver" -> {
                    binding.iBtnAccountNaver.setImageResource(R.drawable.ic_login_naver_18)
                    binding.iBtnAccountGoogle.setImageResource(R.drawable.ic_login_google_unselected_18)
                }
            }
        } catch (e: Exception) {
            Log.d("AccountFragment - Get Nav Data", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarAccount.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 프로필 공개 여부 버튼 클릭
        binding.switchAccountProfile.setOnCheckedChangeListener { _, isChecked ->
            val myPageService = MyPageService()
            myPageService.setCommonView(this)

            // 프로필 공개
            if (isChecked) {
                myPageService.showProfile(UserShowProfileChangeModel(true))
            }
            // 프로필 비공개
            else {
                myPageService.showProfile(UserShowProfileChangeModel(false))
            }
        }

        // 회원 탈퇴 버튼 클릭
        binding.tvAccountWithdrawal.setOnClickListener {
            // 회원 탈퇴 화면 1로 이동
            val actionToWithdrawal1: NavDirections = AccountFragmentDirections.actionAccountFrmToWithdrawal1Frm(myPageItem.loginType)
            view?.findNavController()?.navigate(actionToWithdrawal1)
        }
    }

    // 프로필 공개/비공개 전환 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> Log.d("AccountFragment", "Profile Success")
        }
    }

    // 프로필 공개/비공개 전환 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        Log.d("AccountFragment", "$status $message Profile Failure")

        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401, 404 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }

    }
}