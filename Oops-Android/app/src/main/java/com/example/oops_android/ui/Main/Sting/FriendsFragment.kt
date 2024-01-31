package com.example.oops_android.ui.Main.Sting

import androidx.navigation.findNavController
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentFriendsBinding
import com.example.oops_android.ui.Base.BaseFragment

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
        newFriendsAdapter.addNewFriendsList(FriendsItem(0, "강", "이미지", 0))
        newFriendsAdapter.addNewFriendsList(FriendsItem(1, "웁스", "이미지", 2))
        newFriendsAdapter.addNewFriendsList(FriendsItem(2, "강아지", "이미지", 3))
        newFriendsAdapter.addNewFriendsList(FriendsItem(2, "짜장이랑", "이미지", 3))
        newFriendsAdapter.addNewFriendsList(FriendsItem(3, "지구지킴이", "이미지", 3))
        newFriendsAdapter.addNewFriendsList(FriendsItem(4, "이지밥입니다", "이미지", 3))
        newFriendsAdapter.addNewFriendsList(FriendsItem(5, "이지밥입니다", "이미지", 2))
        newFriendsAdapter.addNewFriendsList(FriendsItem(6, "이지밥입니다", "이미지", 0))

        // 친구 목록 어댑터 연결 및 데이터 등록
        val oldFriendsAdapter = OldFriendsListAdapter(requireContext()) // 친구 목록 어댑터
        binding.rvFriendsOld.adapter = oldFriendsAdapter
        oldFriendsAdapter.addOldFriendsList(FriendsItem(0, "강", "이미지", 1))
        oldFriendsAdapter.addOldFriendsList(FriendsItem(1, "웁스", "이미지", 1))
        oldFriendsAdapter.addOldFriendsList(FriendsItem(2, "강아지", "이미지", 1))
        oldFriendsAdapter.addOldFriendsList(FriendsItem(3, "짜장이랑", "이미지", 1))
        oldFriendsAdapter.addOldFriendsList(FriendsItem(4, "지구지킴이", "이미지", 1))
        oldFriendsAdapter.addOldFriendsList(FriendsItem(5, "이지밥입니다", "이미지", 1))
    }
}