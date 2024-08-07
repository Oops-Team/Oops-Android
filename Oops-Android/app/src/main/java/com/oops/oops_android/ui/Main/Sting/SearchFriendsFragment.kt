package com.oops.oops_android.ui.Main.Sting

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Sting.Api.StingService
import com.oops.oops_android.data.remote.Sting.Api.UsersView
import com.oops.oops_android.data.remote.Sting.Model.StingFriendIdModel
import com.oops.oops_android.data.remote.Sting.Model.StingFriendModel
import com.oops.oops_android.databinding.FragmentSearchFriendsBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.getNickname
import org.json.JSONException
import org.json.JSONObject
import java.util.Random

/* 사용자 목록 검색 화면 */
class SearchFriendsFragment: BaseFragment<FragmentSearchFriendsBinding>(FragmentSearchFriendsBinding::inflate), UsersView, CommonView {

    private var keyword: String? = null // 사용자가 입력한 검색 키워드
    private var keywordList = ArrayList<FriendsItem>() // 검색 결과 리스트

    private var searchFriendsAdapter = SearchFriendsListAdapter(keywordList) // 검색 결과 목록 어댑터

    override fun initViewCreated() {
        // 툴 바 제목 설정
        binding.searchFriendsToolbarSub.tvSubToolbarTitle.text = "친구 목록 검색"
    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼을 클릭한 경우
        binding.searchFriendsToolbarSub.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        // 실시간으로 검색 목록 바꾸기
        showUserList()

        // 친구 목록 어댑터 연결
        binding.rvFriendsSearchResult.adapter = searchFriendsAdapter

        // 친구 신청하기 버튼 클릭 이벤트
        searchFriendsAdapter.onItemClickListener1 = { position ->
            // 친구 신청 API 연결
            requestFriends(StingFriendModel(keywordList[position].userName), position)
        }

        // 친구 끊기 버튼 클릭 이벤트
        searchFriendsAdapter.onItemClickListener2 = { position ->
            // 친구 끊기 API 연결
            refuseFriends(keywordList[position].userIdx, false, position, keywordList[position])
        }

        // 콕콕 찌르기 버튼 클릭 이벤트
        searchFriendsAdapter.onItemClickListener3 = { position ->
            // 콕콕 찌르기 API 연결
            stingFriend(keywordList[position].userName)
        }
    }

    // 입력한 검색 키워드에 따른 검색 함수
    private fun showUserList() {
        binding.edtFriendsSearchBox.addTextChangedListener(object : TextWatcher {
            // 검색 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 검색 결과 띄우기
                binding.rvFriendsSearchResult.visibility = View.VISIBLE
            }

            // 검색 중
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 값이 있다면
                if (charSequence != null) {
                    if (charSequence.isNotBlank()) {
                        clickCancelBtn(binding.edtFriendsSearchBox) // 취소 버튼 띄우기
                        Handler(Looper.getMainLooper()).postDelayed({
                            try {
                                keyword = charSequence.toString()
                                getUsers(keyword!!) // 검색 목록 찾기
                            } catch (e: Exception) {
                                Log.w("FriendsFragment - Get Users", e.stackTraceToString())
                            }
                        }, 200L)
                    }
                }
                // 값이 없다면
                else {
                    binding.rvFriendsSearchResult.visibility = View.GONE // 리사이클러뷰 숨기기
                }
            }

            // 검색 후
            override fun afterTextChanged(s: Editable?) {
                keyword = binding.edtFriendsSearchBox.text.toString()
                if (keyword!!.isNotBlank()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        try {
                            getUsers(keyword!!) // 검색 목록 찾기
                        } catch (e: Exception) {
                            Log.w("FriendsFragment - Get Users", e.stackTraceToString())
                        }
                    }, 200L)
                }

                // 취소 버튼 클릭 이벤트
            }
        })
    }

    // 사용자가 입력한 닉네임(키워드)에 따른 검색 함수
    private fun getUsers(keyword: String) {
        val stingService = StingService()
        stingService.setUsersView(this)
        stingService.getUsers(keyword) // 사용자 리스트 조회 API 연결
    }

    // 사용자 리스트 조회 API 연결 성공
    @SuppressLint("NotifyDataSetChanged")
    override fun onGetUsersSuccess(status: Int, message: String, data: JsonObject?) {
        when (status) {
            200 -> {
                // 성공
                try {
                    // data 파싱
                    val jsonObject = JSONObject(data.toString())

                    if (data != null) {
                        binding.lLayoutFriendsNoSearchResult.visibility = View.GONE
                        keywordList.clear()

                        // friendList data
                        val friendList = jsonObject.getJSONArray("friendList")
                        for (i in 0 until friendList.length()) {
                            val subObject = friendList.getJSONObject(i)
                            val userIdx = subObject.getLong("userIdx")
                            val userName = subObject.getString("userName")
                            val userImg = subObject.getString("userImg")

                            keywordList.add(FriendsItem(userIdx, userName, userImg, 1))
                        }

                        // notFriendList data
                        val notFriendList = jsonObject.getJSONArray("notFriendList")
                        for (i in 0 until notFriendList.length()) {
                            val subObject = notFriendList.getJSONObject(i)
                            val userIdx = subObject.getLong("userIdx")
                            val userName = subObject.getString("userName")
                            val userImg = subObject.getString("userImg")

                            keywordList.add(FriendsItem(userIdx, userName, userImg, 0))
                        }

                        // 검색 결과가 없는 경우
                        if (keywordList.isEmpty()) {
                            binding.lLayoutFriendsNoSearchResult.visibility = View.VISIBLE
                        }

                        // 어댑터 갱신
                        searchFriendsAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.w("FriendsFragment - Get Users", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
        }
    }

    // 사용자 리스트 조회 API 연결 실패
    override fun onGetUsersFailure(status: Int, message: String) {
        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401, 404 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }

    // 친구 신청하기 API 연결
    private fun requestFriends(name: StingFriendModel, position: Int) {
        val stingService = StingService()
        stingService.setCommonView(this)
        stingService.requestFriends(name, position)
        getHideKeyboard(binding.root) // 키보드 숨기기
    }

    // 친구 끊기 API 연결
    private fun refuseFriends(
        friendId: Long,
        isRefuse: Boolean,
        position: Int,
        friendsItem: FriendsItem
    ) {
        val stingService = StingService()
        stingService.setCommonView(this)
        stingService.refuseFriends(StingFriendIdModel(friendId), isRefuse, position, friendsItem)
        getHideKeyboard(binding.root) // 키보드 숨기기
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
        getHideKeyboard(binding.root) // 키보드 숨기기
    }

    // 친구 신청하기 & 친구 끊기 & 콕콕 찌르기 연결 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                try {
                    if (data != null) {
                        when (message) {
                            // 친구 신청 성공
                            "Request Friends" -> {
                                val tempData = data as StingRequestModel
                                // 팝업 띄우기
                                val acceptDialog = FriendsAcceptDialog(requireContext(), R.layout.dialog_friends_request, R.id.btn_popup_friends_request_confirm, tempData.name)
                                acceptDialog.showFriendsAcceptDialog()

                                // 대기중 상태로 바꾸기
                                keywordList[tempData.position].userState = 2

                                // 어댑터 갱신
                                searchFriendsAdapter.notifyItemChanged(tempData.position)
                            }
                            // 친구 끊기
                            "Refuse Friends" -> {
                                // 팝업 띄우기
                                val acceptDialog = FriendsAcceptDialog(requireContext(), R.layout.dialog_friends_refuse, R.id.btn_popup_friends_refuse_confirm)
                                acceptDialog.showFriendsAcceptDialog()

                                val tempData = data as StingRefuseModel

                                // 리스트에서 삭제
                                keywordList.removeAt(tempData.position)

                                // 어댑터 갱신
                                searchFriendsAdapter.notifyItemChanged(tempData.position)
                            }
                            // 콕콕 찌르기
                            "Sting Friends" -> {
                                showCustomSnackBar(data.toString() + "님을 콕콕 찔렀어요!")
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.w("FriendsFragment - Common Friend", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
            // 상대의 알림 설정이 해제된 상태인 경우
            207 -> {
                if (data != null) {
                    when (message) {
                        // 친구 신청 성공
                        "Request Friends" -> {
                            val tempData = data as StingRequestModel
                            // 팝업 띄우기
                            val acceptDialog = FriendsAcceptDialog(requireContext(), R.layout.dialog_friends_request, R.id.btn_popup_friends_request_confirm, tempData.name)
                            acceptDialog.showFriendsAcceptDialog()

                            // 대기중 상태로 바꾸기
                            keywordList[tempData.position].userState = 2

                            // 어댑터 갱신
                            searchFriendsAdapter.notifyItemChanged(tempData.position)
                        }
                    }
                }
            }
        }
    }

    // 친구 신청하기 & 친구 끊기 & 콕콕 찌르기 연결 실패
    override fun onCommonFailure(status: Int, message: String, data: String?) {
        when (status) {
            // 토큰이 존재하지 않는 경우, 토큰이 만료된 경우, 사용자가 존재하지 않는 경우
            400, 401 -> {
                showToast(resources.getString(R.string.toast_server_session))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
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
                                showToast("친구가 알림 설정을 하지 않아서 콕콕 찌를 수가 없어요!")
                            }

                            "해당 사용자의 FCM 토큰 데이터가 없습니다" -> {
                                showToast("친구가 알림 설정을 하지 않아서 콕콕 찌를 수가 없어요!")
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
                    showToast("이미 친구 신청을 보냈어요")
                }
            }
            412 -> {
                if (message == "콕콕 찌르기를 할 수 없습니다") {
                    showToast("친구가 알림 설정을 하지 않아서 콕콕 찌를 수가 없어요!")
                }
            }
            500 -> {
                Log.e("FriendsFragment - 500", message)

                // 콕콕 찌르기 실패
                if (message == "콕콕 찌르기를 실패했습니다") {
                    showToast(message)
                }
            }
            // 서버의 네트워크 에러인 경우
            -1 -> {
                showToast(resources.getString(R.string.toast_server_error))
            }
            // 알 수 없는 오류인 경우
            else -> {
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }
}