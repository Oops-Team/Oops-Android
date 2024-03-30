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
import com.oops.oops_android.data.remote.Sting.Api.StingService
import com.oops.oops_android.data.remote.Sting.Api.UsersView
import com.oops.oops_android.databinding.FragmentSearchFriendsBinding
import com.oops.oops_android.ui.Base.BaseFragment
import org.json.JSONException
import org.json.JSONObject

/* 사용자 목록 검색 화면 */
class SearchFriendsFragment: BaseFragment<FragmentSearchFriendsBinding>(FragmentSearchFriendsBinding::inflate), UsersView {

    private var keyword: String? = null // 사용자가 입력한 검색 키워드
    private var keywordList = ArrayList<FriendsItem>() // 검색 결과 리스트

    private var searchFriendsAdapter = SearchFriendsListAdapter(keywordList) // 검색 결과 목록 어댑터

    override fun initViewCreated() {
        // 툴 바 제목 설정
        binding.searchFriendsToolbarSub.tvSubToolbarTitle.text = getString(R.string.sting_friends)
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

        // 화면 바깥 클릭 시 키보드 내리기
        binding.lLayoutSearchFriendsTop.setOnClickListener {
            getHideKeyboard(binding.root)
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
        showToast(resources.getString(R.string.toast_server_error))
    }
}