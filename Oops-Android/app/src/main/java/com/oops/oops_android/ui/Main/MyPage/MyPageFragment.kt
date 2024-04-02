package com.oops.oops_android.ui.Main.MyPage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonObject
import com.navercorp.nid.NaverIdLoginSDK
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Api.MyPageView
import com.oops.oops_android.databinding.FragmentMyPageBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.clearToken
import org.json.JSONException
import org.json.JSONObject

// 마이 페이지 화면
class MyPageFragment: BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate), MyPageView {

    // 사용자 정보
    private var myPageItem: MyPageItem? = null

    override fun initViewCreated() {
        mainActivity?.hideBnv(false)
    }

    override fun initAfterBinding() {
        // 마이페이지 조회 API 연결
        getMyPage()

        // 공지형 팝업을 클릭한 경우
        binding.lLayoutMyPageCommentNotice.setOnClickListener {
            // 공지사항 화면으로 이동
            val actionToNotice: NavDirections = MyPageFragmentDirections.actionMyPageFrmToNoticeFrm()
            view?.findNavController()?.navigate(actionToNotice)
        }

        // 계정 설정 탭을 클릭한 경우
        binding.lLayoutMyPageAccount.setOnClickListener {
            val actionToAccount: NavDirections = MyPageFragmentDirections.actionMyPageFrmToAccountFrm(myPageItem)
            view?.findNavController()?.navigate(actionToAccount)
        }

        // 알림 설정 탭을 클릭한 경우
        binding.lLayoutMyPageAlarm.setOnClickListener {
            val actionToAlert: NavDirections = MyPageFragmentDirections.actionMyPageFrmToAlertFrm()
            view?.findNavController()?.navigate(actionToAlert)
        }

        // 로그아웃을 클릭한 경우
        binding.tvMyPageLogout.setOnClickListener {
            clearToken()
            val userDB = AppDatabase.getUserDB()!! // room db의 user db
            val loginId = userDB.userDao().getLoginId()
            if (loginId == "naver") {
                NaverIdLoginSDK.logout() // 네이버 로그인 로그아웃
            }

            // 로그인 화면으로 이동
            showToast("로그아웃했습니다")
            requireActivity().let {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
    }

    // 마이페이지 조회 API 연결
    private fun getMyPage() {
        val myPageService = MyPageService()
        myPageService.setMyPageView(this)
        myPageService.getMyPage()
    }

    // 마이페이지 조회 성공
    override fun onGetMyPageSuccess(status: Int, message: String, data: JsonObject?) {
        when (status) {
            200 -> {
                try {
                    // json data 파싱하기
                    val jsonObject = JSONObject(data.toString())

                    // data 파싱 및 뷰에 적용
                    val loginType = jsonObject.getString("loginType")
                    val userEmail = jsonObject.getString("userEmail")
                    val isPublic = jsonObject.getBoolean("isPublic")

                    // 사용자 이름
                    val userName = jsonObject.getString("userName")
                    binding.tvMyPageName.text = userName

                    // 아이템 클래스에 저장
                    myPageItem = MyPageItem(loginType, userEmail, userName, isPublic)

                    // 프로필 사진
                    val userImgUrl = jsonObject.getString("userImgURI")
                    Glide.with(requireContext())
                        .load(userImgUrl)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.ic_friends_profile_default_50)
                        .into(binding.ivMyPageProfile)

                    // 공지
                    val comment = jsonObject.getString("comment")
                    when (jsonObject.getInt("commentType")) {
                        // 공지성 멘트라면
                        1 -> {
                            binding.lLayoutMyPageCommentNotice.visibility = View.VISIBLE
                            binding.lLayoutMyPageCommentTip.visibility = View.GONE
                            binding.lLayoutMyPageCommentDefault.visibility = View.GONE
                            binding.tvMyPageCommentNotice.text = comment
                        }
                        // tip성 멘트라면
                        2 -> {
                            binding.lLayoutMyPageCommentTip.visibility = View.VISIBLE
                            binding.lLayoutMyPageCommentDefault.visibility = View.GONE
                            binding.lLayoutMyPageCommentNotice.visibility = View.GONE
                            binding.tvMyPageCommentTip.text = comment
                        }
                        // 일반 멘트라면
                        3 -> {
                            binding.lLayoutMyPageCommentDefault.visibility = View.VISIBLE
                            binding.lLayoutMyPageCommentNotice.visibility = View.GONE
                            binding.lLayoutMyPageCommentTip.visibility = View.GONE
                            binding.tvMyPageCommentDefault.text = comment
                        }
                    }

                } catch (e: JSONException) {
                    Log.w("MyPageFragment - Get MyPage", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
        }
    }

    // 마이페이지 조회 실패
    override fun onGetMyPageFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}