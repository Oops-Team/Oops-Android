package com.oops.oops_android.ui.Main.Sting

import android.util.Log
import android.view.KeyEvent
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonArray
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Sting.Api.StingService
import com.oops.oops_android.data.remote.Sting.Api.StingView
import com.oops.oops_android.data.remote.Sting.Model.StingFriendIdModel
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import com.oops.oops_android.databinding.FragmentFriendsBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.utils.getNickname
import org.json.JSONArray
import org.json.JSONException
import java.util.Random

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(FragmentFriendsBinding::inflate), StingView, CommonView {

    private var newFriendsAdapter: NewFriendsListAdapter? = null
    private var oldFriendsAdapter: OldFriendsListAdapter? = null

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // 바텀 네비게이션 숨기기

        binding.friendsToolbarSub.tvSubToolbarTitle.text = getString(R.string.sting_friends)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 핸드폰의 뒤로가기 버튼 클릭 시
                // 홈 화면으로 이동
                val actionToHome = FriendsFragmentDirections.actionFriendsFrmToHomeFrm()
                findNavController().navigate(actionToHome)
            }
        })
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼을 누른 경우
        binding.friendsToolbarSub.ivSubToolbarBack.setOnClickListener {
            // 홈 화면으로 이동
            val actionToHome = FriendsFragmentDirections.actionFriendsFrmToHomeFrm()
            findNavController().navigate(actionToHome)
        }

        // 친구 리스트 조회 API 연결
        getFriends()

        // 친구 신청 목록 어댑터 연결 및 데이터 등록
        newFriendsAdapter = NewFriendsListAdapter(requireContext()) // 친구 신청 목록 어댑터
        binding.rvFriendsNew.adapter = newFriendsAdapter

        // 친구 목록 어댑터 연결 및 데이터 등록
        oldFriendsAdapter = OldFriendsListAdapter(requireContext()) // 친구 목록 어댑터
        binding.rvFriendsOld.adapter = oldFriendsAdapter

        // 검색창 버튼을 클릭한 경우
        binding.edtFriendsBox.setOnClickListener {
            // 검색 화면으로 이동
            val actionToSearchFriends: NavDirections = FriendsFragmentDirections.actionFriendsFrmToSearchFriendsFrm()
            view?.findNavController()?.navigate(actionToSearchFriends)
        }

        // 친구 신청 수락 버튼을 클릭한 경우
        newFriendsAdapter?.onFriendsItemClickListener1 = {  position ->
            // 친구 신청 수락 API 연결
            acceptFriends(
                newFriendsAdapter?.getNewFriend(position)!!.userIdx,
                position,
                newFriendsAdapter?.getNewFriend(position)!!
            )
        }

        // 친구 신청 거절 버튼을 클릭한 경우
        newFriendsAdapter?.onFriendsItemClickListener2 = { position ->
            // 친구 신청 거절 API 연결
            refuseFriends(newFriendsAdapter?.getNewFriend(
                position)!!.userIdx,
                true,
                position,
                newFriendsAdapter?.getNewFriend(position)!!
            )
        }

        // 친구 끊기 버튼을 클릭한 경우
        oldFriendsAdapter?.onOldFriendsItemClickListener1 = { position ->
            // 친구를 끊을 것인지 재확인하는 팝업 띄우기
            val friendsDeleteDialog = FriendsDeleteDialog(requireContext())
            friendsDeleteDialog.showFriendsDeleteDialog()
            friendsDeleteDialog.setOnClickedListener(object : FriendsDeleteDialog.FriendsDeleteBtnClickListener {
                // 예 버튼을 누른 경우
                override fun onClicked() {
                    // 친구 끊기 API 연결
                    refuseFriends(
                        oldFriendsAdapter?.getOldFriend(position)!!.userIdx,
                        false,
                        position,
                        oldFriendsAdapter?.getOldFriend(position)!!
                    )
                }
            })
        }

        // 콕콕 찌르기 버튼을 클릭한 경우
        oldFriendsAdapter?.onOldFriendsItemClickListener2 = { position ->
            // 콕콕 찌르기 API 연결
            stingFriend(oldFriendsAdapter?.getOldFriend(position)!!.userName)
        }
    }

    // 친구 리스트 조회 API 연결
    private fun getFriends() {
        val stingService = StingService()
        stingService.setStingView(this)
        stingService.getFriends()
    }

    // 친구 리스트 조회 API 연결 성공
    override fun onGetFriendsSuccess(status: Int, message: String, data: JsonArray?) {
        when (status) {
            200 -> {
                try {
                    val jsonArray = JSONArray(data.toString())

                    for (i in 0 until jsonArray.length()) {
                        val subJsonObject = jsonArray.getJSONObject(i)
                        val userIdx = subJsonObject.getLong("userIdx")
                        val userName = subJsonObject.getString("userName")
                        val userImg = subJsonObject.getString("userImg")
                        val userState = subJsonObject.getInt("userState")

                        when (userState) {
                            // 친구 요청이 들어 온 경우, 대기 중인 경우
                            3, 2 -> {
                                newFriendsAdapter?.addNewFriendsList(FriendsItem(userIdx, userName, userImg, userState))
                            }
                            // 친구인 경우
                            1 -> {
                                oldFriendsAdapter?.addOldFriendsList(FriendsItem(userIdx, userName, userImg, userState))
                            }
                        }
                    }

                } catch (e: JSONException) {
                    Log.w("FriendsFragment - Get Friends", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
        }
    }

    // 친구 리스트 조회 API 연결 실패
    override fun onGetFriendsFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }

    // 친구 신청 수락 API 연결
    private fun acceptFriends(friendId: Long, position: Int, newFriend: FriendsItem) {
        val stingService = StingService()
        stingService.setCommonView(this)
        stingService.acceptFriends(StingFriendIdModel(friendId), position, newFriend)
    }

    // 친구 끊기 & 친구 거절
    private fun refuseFriends(
        friendId: Long,
        isRefuse: Boolean,
        position: Int,
        newFriend: FriendsItem
    ) {
        val stingService = StingService()
        stingService.setCommonView(this)
        stingService.refuseFriends(StingFriendIdModel(friendId), isRefuse, position, newFriend)
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

    // 친구 신청 수락 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (message) {
            // 친구 신청 수락 성공
            "Accept Friends" -> {
                // 팝업 띄우기
                val acceptDialog = FriendsAcceptDialog(requireContext(), R.layout.dialog_friends_accept, R.id.btn_popup_friends_accept_confirm)
                acceptDialog.showFriendsAcceptDialog()

                // 뷰 변경 (친구x -> 친구o)
                val item = data as StingAcceptModel
                newFriendsAdapter?.removeFriend(item.position)
                oldFriendsAdapter?.addOldFriendsList(item.newFriend)
            }
            // 친구 끊기 & 거절
            "Refuse Friends" -> {
                // 친구 거절인 경우
                val item = data as StingRefuseModel
                if (item.isRefuse) {
                    // 팝업 띄우기
                    val acceptDialog = FriendsAcceptDialog(requireContext(), R.layout.dialog_friends_refuse, R.id.btn_popup_friends_refuse_confirm)
                    acceptDialog.showFriendsAcceptDialog()

                    // 뷰 변경(리스트내에서 삭제)
                    newFriendsAdapter?.removeFriend(item.position)
                }
                // 친구 끊기인 경우
                else {
                    // 뷰 변경(리스트내에서 삭제)
                    oldFriendsAdapter?.removeFriend(item.position)
                }
            }
            // 콕콕 찌르기
            "Sting Friends" -> {
                showCustomSnackBar(data.toString() + "님을 콕콕 찔렀어요!")
            }
        }
    }

    // 친구 신청 수락 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            207 -> {
                // 요청은 했으나, 알림 설정 해제로 인해 알림을 보내지 않는 경우
                // 친구 신청, 끊기 ok
                Log.e("FriendsFragment - 207", message)
            }
            404 -> {
                // 콕콕 찌르기 실패
                when (data) {
                    "Sting" -> {
                        when (message) {
                            "해당 사용자가 존재하지 않습니다" -> {
                                showToast("해당 사용자가 존재하지 않습니다")
                            }

                            "올바르지 않은 FCM 토큰입니다" -> {
                                showToast("요청을 처리할 수 없습니다 재로그인해주세요")
                            }
                        }
                    }

                    // 친구 신청 실패
                    "Request" -> {
                        when (message) {
                            "신청하려는 사용자가 존재하지 않습니다" -> {
                                showToast("해당 사용자가 존재하지 않습니다")
                            }
                        }
                    }

                    // 친구 수락 실패
                    "Accept" -> {
                        when (message) {
                            "수락할 사용자가 존재하지 않습니다" -> {
                                showToast("해당 사용자가 존재하지 않습니다")
                            }
                        }
                    }

                    // 친구 삭제 실패
                    "Refuse" -> {
                        when (message) {
                            "해당 사용자가 존재하지 않습니다" -> {
                                showToast("해당 사용자가 존재하지 않습니다")
                            }
                        }
                    }
                }
            }
            // 이미 친구 신청한 사용자인 경우
            409 -> {
                Log.e("FriendsFragment - 409", message)
                if (message == "이미 친구 신청한 사용자입니다") {
                    showToast("이미 서로 친구에요!")
                }
            }
            412 -> {
                if (message == "콕콕 찌르기를 할 수 없습니다") {
                    showToast("알림 설정을 동의해주세요!")
                }
            }
            500 -> {
                Log.e("FriendsFragment - 500", message)

                // 콕콕 찌르기 실패
                if (message == "콕콕 찌르기를 실패했습니다") {
                    showToast(message)
                }
            }
            else -> showToast(resources.getString(R.string.toast_server_error))
        }
    }
}