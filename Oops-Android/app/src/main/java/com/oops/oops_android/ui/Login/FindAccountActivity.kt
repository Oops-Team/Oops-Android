package com.oops.oops_android.ui.Login

import com.google.android.material.tabs.TabLayoutMediator
import com.oops.oops_android.R
import com.oops.oops_android.databinding.ActivityFindAccountBinding
import com.oops.oops_android.ui.Base.BaseActivity

/* ID/PW 찾기 화면 */
class FindAccountActivity: BaseActivity<ActivityFindAccountBinding>(ActivityFindAccountBinding::inflate) {

    override fun beforeSetContentView() {
    }

    override fun connectOopsAPI(token: String?, loginId: String?) {
    }

    override fun initAfterBinding() {
        val findAccountAdapter = FindAccountVPAdapter(this@FindAccountActivity)
        binding.vpFindAccount.adapter = findAccountAdapter

        // tab과 viewpager2 연결
        val accountTab = arrayListOf(getString(R.string.find_id), getString(R.string.find_pw)) // tab 목록

        TabLayoutMediator(binding.tabLayoutFindAccount, binding.vpFindAccount) { tab, position ->
            tab.text = accountTab[position]
        }.attach()
    }
}