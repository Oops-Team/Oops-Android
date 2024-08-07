package com.oops.oops_android.ui.Main.MyPage

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentTermsBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.setOnSingleClickListener

/* 회원 탈퇴 과정 중 개인정보 처리 방침 화면 */
class TermsFragment: BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate) {
    private lateinit var withdrawalItem: WithdrawalItem // 전달받은 회원 탈퇴 사유 데이터
    private var loginId = "" // 회원탈퇴하려고 하는 계정 유형
    private var positionFlag: Boolean = true

    override fun initViewCreated() {
        // 툴바 타이틀 설정
        binding.toolbarTerms.tvSubToolbarTitle.text = getString(R.string.withdrawal_title)

        // 뒤로 가기 버튼 숨기기
        binding.toolbarTerms.ivSubToolbarBack.visibility = View.GONE

        // 전달 받은 데이터 저장
        val args: TermsFragmentArgs by navArgs()
        try {
            withdrawalItem = args.tempWithdrawalItem!!
            loginId = args.loginId

        } catch (e: Exception) {
            Log.e("TermsFragment - Nav Item", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 버튼 클릭 이벤트
        binding.btnTermsScrollToEnd.setOnSingleClickListener {
            // 버튼 이름이 아래로 스크롤하기 라면
            if (binding.btnTermsScrollToEnd.text == getString(R.string.btn_scroll_down)) {
                binding.svTerms.post {
                    // 하단으로 스크롤
                    //binding.svTerms.fullScroll(ScrollView.FOCUS_DOWN)
                    ObjectAnimator.ofInt(binding.svTerms, "scrollY", 10000).setDuration(1500).start()
                }
            }
            // 버튼 이름이 확인했습니다 라면
            else {
                // 이전 화면으로 이동
                val actionToWithdrawal2 = TermsFragmentDirections.actionTermsFrmToWithdrawal2Frm(
                        withdrawalItem,
                        true,
                        loginId
                    )
                view?.findNavController()?.navigate(actionToWithdrawal2)
            }
        }

        // 화면 하단까지 스크롤을 했다면
        binding.svTerms.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (positionFlag) {
                if ((!view.canScrollVertically(1))) {
                    // 버튼 텍스트 바꾸기
                    binding.btnTermsScrollToEnd.text = getString(R.string.btn_check_read)
                    positionFlag = false
                }
            }
        }
    }
}