package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.oops.oops_android.ApplicationClass
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Model.UserWithdrawalModel
import com.oops.oops_android.databinding.FragmentWithdrawal2Binding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.AlarmUtils
import com.oops.oops_android.utils.ButtonUtils
import com.oops.oops_android.utils.clearSpf

class Withdrawal2Fragment: BaseFragment<FragmentWithdrawal2Binding>(FragmentWithdrawal2Binding::inflate), CommonView {
    private lateinit var withdrawalItem: WithdrawalItem // 탈퇴 사유 데이터
    private var isReadTerms: Boolean = false // 개인정보 처리 방침 읽음 여부
    private var loginId = "" // 회원 탈퇴하려고 하는 계정 유형

    private var isEnable = false // 탈퇴할게요 버튼 활성화 여부

    override fun initViewCreated() {
        // 툴 바 텍스트 설정
        binding.toolbarWithdrawal2.tvSubToolbarTitle.text = getString(R.string.withdrawal_title)

        // 전달 받은 데이터 저장
        val args: Withdrawal2FragmentArgs by navArgs()
        try {
            withdrawalItem = args.withdrawalItem!!
            isReadTerms = args.isReadTerms
            loginId = args.loginId

        } catch (e: Exception) {
            Log.e("Withdrawal2Fragment - Nav Item", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭 이벤트
        binding.toolbarWithdrawal2.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // TermsFragment 화면에서 뒤로가기 버튼을 클릭한 경우
        if (binding.cBoxWithdrawal2Check1.isChecked) {
            binding.btnWithdrawal2End.setTextAppearance(R.style.WideButtonEnableStyle) // 버튼 색상 전환
            binding.btnWithdrawal2End.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Main_500))
        }

        // 개인 정보 처리 방침 버튼 클릭 이벤트
        binding.lLayoutWithdrawal2Terms.setOnClickListener {
            val actionToTerms: NavDirections = Withdrawal2FragmentDirections.actionWithdrawal2FrmToTermsFrm(
                    withdrawalItem,
                    loginId
                )
            view?.findNavController()?.navigate(actionToTerms) // 개인 정보 처리 방침 화면으로 이동
        }

        // 개인정보 처리 방침을 확인했다면
        if (isReadTerms) {
            // 처리 방침 상태 변경
            binding.cBoxWithdrawal2Check1.isChecked = true
            binding.cBoxWithdrawal2Check2.isChecked = true

            // 버튼 활성화
            updateButtonUI()
        }

        /* 체크박스 3개 클릭 이벤트 */
        binding.cBoxWithdrawal2Check1.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check1.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check1.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check1.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        binding.cBoxWithdrawal2Check2.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check2.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check2.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check2.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        binding.cBoxWithdrawal2Check2.setOnCheckedChangeListener { _, isChecked ->
            // 개인정보 처리 방침을 안 확인했다면
            if (!isReadTerms) {
                binding.cBoxWithdrawal2Check2.setButtonDrawable(R.drawable.ic_withdrawal_checkbox_unselected)
                binding.cBoxWithdrawal2Check2.isChecked = false
                showCustomSnackBar(getString(R.string.snackbar_read_terms)) // 스낵바 띄우기
            }
            else {
                // 체크 해제 <-> 선택
                binding.cBoxWithdrawal2Check2.isChecked = isChecked
            }

            // 탈퇴할게요 버튼 색상 전환
            checkButtonCondition()
        }

        // 탈퇴할게요 버튼 클릭
        binding.btnWithdrawal2End.setOnClickListener {
            if (isEnable) {
                // 탈퇴 여부 재확인 팝업 띄우기
                val withdrawalDialog = WithdrawalDialog(requireContext())
                withdrawalDialog.showWithdrawalDialog()
                withdrawalDialog.setOnClickedListener(object : WithdrawalDialog.WithdrawalBtnClickListener {
                    // 회원 탈퇴 버튼을 클릭한 경우
                    override fun onClicked() {
                        // 회원 탈퇴 API 연동
                        oopsWithdrawal(UserWithdrawalModel(withdrawalItem.reasonType, withdrawalItem.subReason))
                    }
                })
            }
        }
    }

    // 탈퇴할게요 버튼 활성화 여부 체크 함수
    private fun updateButtonUI() {
        // 버튼 색상 전환
        isEnable = true
        ButtonUtils().setColorAnimation(binding.btnWithdrawal2End)
    }

    // 탈퇴할게요 버튼 활성화 로직 체크 함수
    private fun checkButtonCondition() {
        // 모든 버튼이 체크가 되어 있고, 개인정보 처리 방침을 확인했다면
        if (binding.cBoxWithdrawal2Check1.isChecked && binding.cBoxWithdrawal2Check2.isChecked && isReadTerms) {
            if (!isEnable) {
                updateButtonUI() // 색상 전환
            }
        }
        // 아니라면
        else {
            // 색상 바꾸기 및 버튼 비활성화
            isEnable = false
            binding.btnWithdrawal2End.setTextAppearance(R.style.WideButtonDisableStyle)
            binding.btnWithdrawal2End.setBackgroundColor(ApplicationClass.applicationContext().getColor(R.color.Gray_100))
        }
    }

    // 탈퇴하기 API 연결
    private fun oopsWithdrawal(userWithdrawalModel: UserWithdrawalModel) {
        val myPageService = MyPageService()
        myPageService.setCommonView(this)
        myPageService.oopsWithdrawal(userWithdrawalModel)
    }

    // 탈퇴하기 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 토큰 삭제
                clearSpf()

                // 유저 데이터 삭제
                val userDB = AppDatabase.getUserDB()!! // room db의 user db
                userDB.userDao().deleteAllUser()

                // 기존에 저장되어 있던 모든 알람 삭제(db, 알람 취소)
                AlarmUtils.setCancelAllAlarm(requireContext())

                // 네이버 애플리케이션 연동 해제
                if (loginId == "naver") {
                    NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                        override fun onError(errorCode: Int, message: String) {
                            onFailure(errorCode, message)
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Log.d("Withdrawal2Fragment", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                            Log.d("Withdrawal2Fragment", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                        }

                        override fun onSuccess() {
                            // 삭제 성공
                        }
                    })
                }
                // 구글 애플리케이션 연동 해제
                else if (loginId == "google") {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_login_client_id))
                        .requestEmail()
                        .build()

                    // 객체 생성
                    val googleSignInClient = GoogleSignIn.getClient(mainActivity!!, gso)

                    // 계정 삭제
                    googleSignInClient.revokeAccess()
                }

                showToast(resources.getString(R.string.toast_user_withdrawal))

                // 로그인 화면으로 이동
                mainActivity?.startActivityWithClear(LoginActivity::class.java)
                //val actionToLogin = Withdrawal2FragmentDirections.actionWithdrawal2FrmToLoginActivity()
                //view?.findNavController()?.navigate(actionToLogin)
            }
        }
    }

    // 탈퇴하기 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
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