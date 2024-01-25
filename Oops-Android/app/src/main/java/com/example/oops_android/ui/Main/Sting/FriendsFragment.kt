package com.example.oops_android.ui.Main.Sting

import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentFriendsBinding
import com.example.oops_android.ui.Base.BaseFragment

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {

    override fun initViewCreated() {
        binding.friendsToolbarSub.tvSubToolbarTitle.text = getString(R.string.sting_friends)
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 누른 경우
        binding.friendsToolbarSub.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}