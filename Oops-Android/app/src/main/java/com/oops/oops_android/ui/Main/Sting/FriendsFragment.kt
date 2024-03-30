package com.oops.oops_android.ui.Main.Sting

import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentFriendsBinding
import com.oops.oops_android.ui.Base.BaseFragment

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate) {

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // 바텀 네비게이션 숨기기

        binding.friendsToolbarSub.tvSubToolbarTitle.text = getString(R.string.sting_friends)
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 누른 경우
        binding.friendsToolbarSub.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 친구 신청 목록 어댑터 연결 및 데이터 등록
        val newFriendsAdapter = NewFriendsListAdapter(requireContext()) // 친구 신청 목록 어댑터
        binding.rvFriendsNew.adapter = newFriendsAdapter

        // 친구 목록 어댑터 연결 및 데이터 등록
        val oldFriendsAdapter = OldFriendsListAdapter(requireContext()) // 친구 목록 어댑터
        binding.rvFriendsOld.adapter = oldFriendsAdapter

        // 검색창 버튼을 클릭한 경우
        binding.edtFriendsBox.setOnClickListener {
            // 검색 화면으로 이동
            val actionToSearchFriends: NavDirections = FriendsFragmentDirections.actionFriendsFrmToSearchFriendsFrm()
            view?.findNavController()?.navigate(actionToSearchFriends)
        }
    }
}