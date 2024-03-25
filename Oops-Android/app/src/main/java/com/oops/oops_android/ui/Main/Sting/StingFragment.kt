package com.oops.oops_android.ui.Main.Sting

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonArray
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Sting.Api.StingService
import com.oops.oops_android.data.remote.Sting.Api.StingView
import com.oops.oops_android.databinding.FragmentStingBinding
import com.oops.oops_android.ui.Base.BaseFragment
import org.json.JSONArray
import org.json.JSONException

/* 콕콕 찌르기 화면 */
class StingFragment: BaseFragment<FragmentStingBinding>(FragmentStingBinding::inflate), StingView {

    // 외출 30분 전 친구 리스트
    private var friendList = ArrayList<FriendsItem>()

    override fun initViewCreated() {
        mainActivity?.hideBnv(false) // 나타내기
    }

    override fun initAfterBinding() {
        // 외출 30분 전 친구 리스트 조회 API 연결
        get30mFriends()

        // 친구 목록 버튼을 누른 경우
        binding.btnStingFriends.setOnClickListener {
            val actionToFriends : NavDirections = StingFragmentDirections.actionStingFrmToFriendsFrm()
            view?.findNavController()?.navigate(actionToFriends)
        }
    }

    // 외출 30분 전 친구 리스트 조회 API 연결
    private fun get30mFriends() {
        val stingService = StingService()
        stingService.setStingView(this)
        stingService.get30mFriends()
    }

    // 외출 30분 전 친구 리스트 조회 성공
    override fun onGet30mFriendsSuccess(status: Int, message: String, data: JsonArray?) {
        when (status) {
            200 -> {
                try {
                    if (!data!!.isJsonNull) {
                        val jsonArray = JSONArray(data.toString())

                        // 친구 데이터
                        for (i in 0 until jsonArray.length()) {
                            val subObject = jsonArray.getJSONObject(i)
                            val userIdx = subObject.getLong("userIdx")
                            val userName = subObject.getString("userName")
                            val userImg = subObject.getString("userImg")

                            friendList.add(FriendsItem(userIdx, userName, userImg, 1))
                        }

                        /* 뷰에 데이터 적용 */
                        // 외출 임박한 친구가 1명이라면
                        when (friendList.size) {
                            1 -> {
                                binding.cLayoutStingFriends1.visibility = View.VISIBLE
                                setProfileImg(binding.ivStingFriend11, friendList[0].userImg)
                            }

                            // 외출 임박한 친구가 2명이라면
                            2 -> {
                                binding.cLayoutStingFriends2.visibility = View.VISIBLE
                                setProfileImg(binding.ivStingFriend21, friendList[0].userImg)
                                setProfileImg(binding.ivStingFriend22, friendList[1].userImg)
                            }

                            // 외출 임박한 친구가 3명이라면
                            3 -> {
                                binding.cLayoutStingFriends3.visibility = View.VISIBLE
                                setProfileImg(binding.ivStingFriend31, friendList[0].userImg)
                                setProfileImg(binding.ivStingFriend32, friendList[1].userImg)
                                setProfileImg(binding.ivStingFriend33, friendList[2].userImg)
                            }

                            // 외출 임박한 친구가 4명이라면
                            4 -> {
                                binding.cLayoutStingFriends4.visibility = View.VISIBLE
                                setProfileImg(binding.ivStingFriend41, friendList[0].userImg)
                                setProfileImg(binding.ivStingFriend42, friendList[1].userImg)
                                setProfileImg(binding.ivStingFriend43, friendList[2].userImg)
                                setProfileImg(binding.ivStingFriend44, friendList[3].userImg)
                            }

                            // 외출 임박한 친구가 5명이라면
                            else -> {
                                binding.cLayoutStingFriends5.visibility = View.VISIBLE
                                setProfileImg(binding.ivStingFriend51, friendList[0].userImg)
                                setProfileImg(binding.ivStingFriend52, friendList[1].userImg)
                                setProfileImg(binding.ivStingFriend53, friendList[2].userImg)
                                setProfileImg(binding.ivStingFriend54, friendList[3].userImg)
                                setProfileImg(binding.ivStingFriend55, friendList[4].userImg)
                            }
                        }
                    }

                } catch (e: JSONException) {
                    Log.w("StingFragment - Get 30m Friends", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
        }
    }

    // 이미지뷰에 서버에서 받아온 친구 프로필 사진 넣기
    private fun setProfileImg(imageView: ImageView, userImg: String) {
        Glide.with(requireContext())
            .load(userImg)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_friends_profile_default_50)
            .placeholder(R.drawable.ic_friends_profile_default_50)
            .into(imageView)
    }

    // 외출 30분 전 친구 리스트 조회 실패
    override fun onGet30mFriendsFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}