package com.oops.oops_android.ui.Main.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Api.MyPageView
import com.oops.oops_android.databinding.FragmentMyPageBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import com.oops.oops_android.utils.AlarmUtils
import com.oops.oops_android.utils.removeToken
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
            val actionToAlert: NavDirections = MyPageFragmentDirections.actionMyPageFrmToAlertFrm(myPageItem!!.isAlert)
            view?.findNavController()?.navigate(actionToAlert)
        }

        // 프로필 사진을 클릭한 경우
        binding.ivMyPageProfile.setOnClickListener {
            /*// 프로필 사진 변경 바텀 시트 띄우기
            val fragmentManager = requireActivity().supportFragmentManager
            val bottomSheet = ProfileBottomSheetFragment()
            val bundle = Bundle()

            // 프로필 사진이 없다면
            if (myPageItem!!.userImgURI!! == "null") {
                Log.d("확인", "체크")
                bundle.putBoolean("isDefault", true)
                bottomSheet.arguments = bundle
                bottomSheet.show(fragmentManager, "sheet")
            }
            // 프로필 사진이 있다면
            else {
                Log.d("확인", "체크2")
                bundle.putBoolean("isDefault", false)
                bottomSheet.arguments = bundle
                bottomSheet.show(fragmentManager, "sheet")
            }*/

            // 프로필 사진이 없다면
            if (myPageItem!!.userImgURI == "null") {
                val actionToProfileBottomSheet = MyPageFragmentDirections.actionMyPageFrmToProfileBottomSheetFrm(false)
                findNavController().navigate(actionToProfileBottomSheet)
            }
            // 프로필 사진이 있다면
            else {
                val actionToProfileBottomSheet = MyPageFragmentDirections.actionMyPageFrmToProfileBottomSheetFrm(true)
                findNavController().navigate(actionToProfileBottomSheet)
            }
        }

        // 로그아웃을 클릭한 경우
        binding.tvMyPageLogout.setOnClickListener {
            val logoutDialog = LogoutDialog(requireContext())
            logoutDialog.showLogoutDialog()
            logoutDialog.setOnClickedListener(object : LogoutDialog.LogoutBtnClickListener {

                // 로그아웃 버튼을 누른 경우
                override fun onClicked() {
                    removeToken()

                    // 기존에 저장되어 있던 모든 알람 삭제(db, 알람 취소)
                    AlarmUtils.setCancelAllAlarm(requireContext())

                    // 로그인 화면으로 이동
                    showToast("로그아웃했습니다")
                    requireActivity().let {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }
            })
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
                    val isAlert = jsonObject.getBoolean("isAlert")

                    // 사용자 이름
                    val userName = jsonObject.getString("userName")
                    binding.tvMyPageName.text = userName

                    // 프로필 사진
                    val userImgUrl = jsonObject.getString("userImgURI") ?: "null"
                    if (userImgUrl == "null") {
                        Glide.with(requireContext())
                            .load(R.drawable.ic_friends_profile_default_50)
                            .fallback(R.drawable.ic_friends_profile_default_50)
                            .error(R.drawable.ic_friends_profile_default_50)
                            .into(binding.ivMyPageProfile)
                    }
                    // 프로필 사진이 없다면
                    else {
                        Glide.with(requireContext())
                            .load(userImgUrl)
                            .fallback(R.drawable.ic_friends_profile_default_50)
                            .error(R.drawable.ic_friends_profile_default_50)
                            .into(binding.ivMyPageProfile)
                    }

                    // 아이템 클래스에 저장
                    myPageItem = MyPageItem(userImgUrl, loginType, userEmail, userName, isPublic, isAlert)

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
                showToast(resources.getString(R.string.toast_server_error_to_login))
                mainActivity?.startActivityWithClear(LoginActivity::class.java) // 로그인 화면으로 이동
            }
        }
    }
}