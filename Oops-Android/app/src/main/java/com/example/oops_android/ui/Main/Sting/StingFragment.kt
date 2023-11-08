package com.example.oops_android.ui.Main.Sting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentStingBinding
import com.example.oops_android.ui.BaseFragment

class StingFragment: BaseFragment<FragmentStingBinding>(FragmentStingBinding::inflate) {
    override fun initViewCreated() {
    }

    override fun initAfterBinding() {
        // 친구 목록 버튼을 누른 경우
        binding.btnStingFriends.setOnClickListener {
            val actionToFriends : NavDirections = StingFragmentDirections.actionStingFrmToFriendsFrm()
            view?.findNavController()?.navigate(actionToFriends)
        }
    }

}