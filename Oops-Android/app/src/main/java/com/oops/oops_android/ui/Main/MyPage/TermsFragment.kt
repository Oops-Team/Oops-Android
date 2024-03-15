package com.oops.oops_android.ui.Main.MyPage

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentTermsBinding
import com.oops.oops_android.ui.Base.BaseFragment

/* 회원 탈퇴 과정 중 개인정보 처리 방침 화면 */
class TermsFragment: BaseFragment<FragmentTermsBinding>(FragmentTermsBinding::inflate) {
    private lateinit var withdrawalItem: WithdrawalItem // 전달받은 회원 탈퇴 사유 데이터
    override fun initViewCreated() {
        // 툴바 타이틀 설정
        binding.toolbarTerms.tvSubToolbarTitle.text = getString(R.string.withdrawal_title)

        // 뒤로 가기 버튼 숨기기
        binding.toolbarTerms.ivSubToolbarBack.visibility = View.GONE

        // 전달 받은 데이터 저장
        val args: com.oops.oops_android.ui.MyPage.TermsFragmentArgs by navArgs()
        try {
            withdrawalItem = args.tempWithdrawalItem!!

        } catch (e: Exception) {
            Log.e("TermsFragment - Nav Item", e.stackTraceToString())
        }
    }

    override fun initAfterBinding() {
        // 버튼 클릭 이벤트
        binding.btnTermsScrollToEnd.setOnClickListener {
            // 버튼 이름이 아래로 스크롤하기 라면
            if (binding.btnTermsScrollToEnd.text == getString(R.string.btn_scroll_down)) {
                binding.svTerms.post {
                    // 하단으로 스크롤
                    //binding.svTerms.fullScroll(ScrollView.FOCUS_DOWN)
                    ObjectAnimator.ofInt(binding.svTerms, "scrollY", 10000).setDuration(1000).start()

                    // 버튼 텍스트 바꾸기
                    binding.btnTermsScrollToEnd.text = getString(R.string.btn_check_read)
                }
            }
            // 버튼 이름이 확인했습니다 라면
            else {
                // 이전 화면으로 이동
                val actionToWithdrawal2 =
                    com.oops.oops_android.ui.MyPage.TermsFragmentDirections.actionTermsFrmToWithdrawal2Frm(
                        withdrawalItem,
                        true
                    )
                view?.findNavController()?.navigate(actionToWithdrawal2)
            }
        }
    }
}