package com.oops.oops_android.ui.Login

import com.oops.oops_android.databinding.ActivityMoreTermsBinding
import com.oops.oops_android.ui.Base.BaseActivity

// 개인정보 보관 약관 화면
class MoreTermsActivity: BaseActivity<ActivityMoreTermsBinding>(ActivityMoreTermsBinding::inflate) {

    override fun beforeSetContentView() {
    }

    override fun connectOopsAPI(token: String?) {
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.ivMoreTermsBack.setOnClickListener {
            finish() // 화면 종료(stack x)
        }
    }
}