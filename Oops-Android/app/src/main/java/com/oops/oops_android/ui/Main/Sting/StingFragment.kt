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
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Sting.Api.StingService
import com.oops.oops_android.data.remote.Sting.Api.StingView
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import com.oops.oops_android.databinding.FragmentStingBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.getNickname
import org.json.JSONArray
import org.json.JSONException
import java.util.Random

/* 콕콕 찌르기 화면 */
class StingFragment: BaseFragment<FragmentStingBinding>(FragmentStingBinding::inflate), StingView, CommonView {

    // 외출 30분 전 친구 리스트
    private var friendList = ArrayList<FriendsItem>()

    // 콕콕 찌르기 횟수 제한(10개)
    // 친구 1명
    private var count11: Int = 0

    // 친구 2명
    private var count21: Int = 0
    private var count22: Int = 0

    // 친구 3명
    private var count31: Int = 0
    private var count32: Int = 0
    private var count33: Int = 0

    // 친구 4명
    private var count41: Int = 0
    private var count42: Int = 0
    private var count43: Int = 0
    private var count44: Int = 0

    // 친구 5명
   private var count51: Int = 0
   private var count52: Int = 0
   private var count53: Int = 0
   private var count54: Int = 0
   private var count55: Int = 0

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
    override fun onGetFriendsSuccess(status: Int, message: String, data: JsonArray?) {
        when (status) {
            200 -> {
                try {
                    val jsonArray = JSONArray(data.toString())

                    // 친구 데이터
                    friendList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val subObject = jsonArray.getJSONObject(i)
                        val userIdx = subObject.getLong("userIdx")
                        val userName = subObject.getString("userName")
                        val userImg = subObject.getString("userImg")

                        friendList.add(FriendsItem(userIdx, userName, userImg, 1))
                    }

                    /* 뷰에 데이터 적용 */
                    // 각 사용자마다 클릭 횟수 제한 10회
                    // 외출 임박한 친구가 1명이라면
                    when (friendList.size) {
                        1 -> {
                            binding.cLayoutStingFriends1.visibility = View.VISIBLE
                            setProfileImg(binding.ivStingFriend11, friendList[0].userImg)

                            binding.ivStingFriend11.setOnClickListener {
                                count11++
                                if (count11 <= 9) {
                                    stingFriend(friendList[0].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }
                        }

                        // 외출 임박한 친구가 2명이라면
                        2 -> {
                            binding.cLayoutStingFriends2.visibility = View.VISIBLE
                            setProfileImg(binding.ivStingFriend21, friendList[0].userImg)
                            setProfileImg(binding.ivStingFriend22, friendList[1].userImg)

                            binding.ivStingFriend21.setOnClickListener {
                                count21++
                                if (count21 <= 9) {
                                    stingFriend(friendList[0].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend22.setOnClickListener {
                                count22++
                                if (count22 <= 9) {
                                    stingFriend(friendList[1].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }
                        }

                        // 외출 임박한 친구가 3명이라면
                        3 -> {
                            binding.cLayoutStingFriends3.visibility = View.VISIBLE
                            setProfileImg(binding.ivStingFriend31, friendList[0].userImg)
                            setProfileImg(binding.ivStingFriend32, friendList[1].userImg)
                            setProfileImg(binding.ivStingFriend33, friendList[2].userImg)

                            binding.ivStingFriend31.setOnClickListener {
                                count31++
                                if (count31 <= 9) {
                                    stingFriend(friendList[0].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend32.setOnClickListener {
                                count32++
                                if (count32 <= 9) {
                                    stingFriend(friendList[1].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend33.setOnClickListener {
                                count33++
                                if (count33 <= 9) {
                                    stingFriend(friendList[2].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }
                        }

                        // 외출 임박한 친구가 4명이라면
                        4 -> {
                            binding.cLayoutStingFriends4.visibility = View.VISIBLE
                            setProfileImg(binding.ivStingFriend41, friendList[0].userImg)
                            setProfileImg(binding.ivStingFriend42, friendList[1].userImg)
                            setProfileImg(binding.ivStingFriend43, friendList[2].userImg)
                            setProfileImg(binding.ivStingFriend44, friendList[3].userImg)

                            binding.ivStingFriend41.setOnClickListener {
                                count41++
                                if (count41 <= 9) {
                                    stingFriend(friendList[0].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend42.setOnClickListener {
                                count42++
                                if (count42 <= 9) {
                                    stingFriend(friendList[1].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend43.setOnClickListener {
                                count43++
                                if (count43 <= 9) {
                                    stingFriend(friendList[2].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend44.setOnClickListener {
                                count44++
                                if (count44 <= 9) {
                                    stingFriend(friendList[3].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }
                        }

                        // 외출 임박한 친구가 5명이라면
                        5 -> {
                            binding.cLayoutStingFriends5.visibility = View.VISIBLE
                            setProfileImg(binding.ivStingFriend51, friendList[0].userImg)
                            setProfileImg(binding.ivStingFriend52, friendList[1].userImg)
                            setProfileImg(binding.ivStingFriend53, friendList[2].userImg)
                            setProfileImg(binding.ivStingFriend54, friendList[3].userImg)
                            setProfileImg(binding.ivStingFriend55, friendList[4].userImg)

                            binding.ivStingFriend51.setOnClickListener {
                                count51++
                                if (count51 <= 9) {
                                    stingFriend(friendList[0].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend52.setOnClickListener {
                                count52++
                                if (count52 <= 9) {
                                    stingFriend(friendList[1].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend53.setOnClickListener {
                                count53++
                                if (count53 <= 9) {
                                    stingFriend(friendList[2].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend54.setOnClickListener {
                                count54++
                                if (count54 <= 9) {
                                    stingFriend(friendList[3].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
                            }

                            binding.ivStingFriend55.setOnClickListener {
                                count55++
                                if (count55 <= 9) {
                                    stingFriend(friendList[4].userName)
                                }
                                else {
                                    showCustomSnackBar(resources.getString(R.string.snackbar_sting_10))
                                }
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
    override fun onGetFriendsFailure(status: Int, message: String) {
        when (status) {
            404 -> {

            }
            else -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
        }
    }

    // 콕콕 찌르기 API 연결
    private fun stingFriend(name: String) {
        val stingService = StingService()
        stingService.setCommonView(this)
        //val userDB = AppDatabase.getUserDB()!!
        //val userName = userDB.userDao().getUser().name

        val randomSting = listOf("${getNickname()} 님이 콕콕 찔렀어요!", "${getNickname()} 님이 외출 준비 할 시간이래요", "콕콕! 누군가가 $name 님을 찔렀어요")
        val randomStingIndex = Random().nextInt(randomSting.size)
        stingService.stingFriend(StingFriendModel(name, randomSting[randomStingIndex]))
    }

    // 콕콕 찌르기 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                showCustomSnackBar(data.toString() + "님을 콕콕 찔렀어요!")
            }
        }
    }

    // 콕콕 찌르기 실패
    override fun onCommonFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}