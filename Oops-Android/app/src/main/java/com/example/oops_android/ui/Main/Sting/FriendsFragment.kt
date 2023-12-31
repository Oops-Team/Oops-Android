package com.example.oops_android.ui.Main.Sting

import androidx.navigation.findNavController
import com.example.oops_android.databinding.FragmentFriendsBinding
import com.example.oops_android.ui.BaseFragment

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {

    override fun initViewCreated() {
        binding.friendsToolbarSub.tvSubToolbarTitle.text = "친구 목록"
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 누른 경우
        binding.friendsToolbarSub.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}