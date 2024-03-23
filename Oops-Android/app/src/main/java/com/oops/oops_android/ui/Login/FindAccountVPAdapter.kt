package com.oops.oops_android.ui.Login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalStateException

/* ID/PW 찾기 화면에서 사용하는 VP 어댑터 */
class FindAccountVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FindIdFragment()
            1 -> FindPwFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}