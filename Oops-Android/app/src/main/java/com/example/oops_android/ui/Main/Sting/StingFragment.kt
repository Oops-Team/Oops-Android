package com.example.oops_android.ui.Main.Sting

import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.oops_android.databinding.FragmentStingBinding
import com.example.oops_android.ui.Base.BaseFragment

class StingFragment: BaseFragment<FragmentStingBinding>(FragmentStingBinding::inflate) {
    override fun initViewCreated() {
        mainActivity?.hideBnv(false) // 나타내기
    }

    override fun initAfterBinding() {
        // 친구 목록 버튼을 누른 경우
        binding.btnStingFriends.setOnClickListener {
            val actionToFriends : NavDirections = StingFragmentDirections.actionStingFrmToFriendsFrm()
            view?.findNavController()?.navigate(actionToFriends)
        }
    }

}