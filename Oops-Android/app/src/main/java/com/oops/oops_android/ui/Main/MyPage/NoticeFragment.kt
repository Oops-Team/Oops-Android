package com.oops.oops_android.ui.Main.MyPage

import android.util.Log
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.gson.JsonArray
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.MyPage.Api.MyPageService
import com.oops.oops_android.data.remote.MyPage.Api.NoticeView
import com.oops.oops_android.databinding.FragmentNoticeBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Login.LoginActivity
import org.json.JSONArray
import org.json.JSONException

/* 공지사항 목록 화면 */
class NoticeFragment: BaseFragment<FragmentNoticeBinding>(FragmentNoticeBinding::inflate), NoticeView {
    private var noticeAdapter: NoticeListAdapter? = null // 공지사항 목록 어댑터

    override fun initViewCreated() {
        mainActivity?.hideBnv(true) // bnv 숨기기

        // 툴 바 제목 설정
        binding.toolbarNotice.tvSubToolbarTitle.text = "공지사항"
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarNotice.ivSubToolbarBack.setOnClickListener {
            // 마이페이지 화면으로 이동
            view?.findNavController()?.popBackStack()
        }

        noticeAdapter = NoticeListAdapter(requireContext())
        binding.rvNotice.adapter = noticeAdapter

        // 공지사항 조회 API 연결
        getNotices()

        // 공지사항 클릭
        noticeAdapter?.onItemClickListener = { position ->
            // 세부 공지사항 조회 화면으로 이동
            val actionToNoticeDetail: NavDirections = NoticeFragmentDirections.actionNoticeFrmToNoticeDetailFrm(noticeAdapter?.getNotice(position))
            view?.findNavController()?.navigate(actionToNoticeDetail)
        }
    }

    // 공지사항 조회 API 연결
    private fun getNotices() {
        val myPageService = MyPageService()
        myPageService.setNoticeView(this)
        myPageService.getNotices()
    }

    // 공지사항 조회 성공
    override fun onGetNoticeSuccess(status: Int, message: String, data: JsonArray) {
        when (status) {
            200 -> {
                try {
                    // json array 파싱하기
                    val jsonArray = JSONArray(data.toString())

                    // 공지사항 목록 불러오기
                    for (i in 0 until jsonArray.length()) {
                        val subJsonObject = jsonArray.getJSONObject(i)
                        val noticeTitle = subJsonObject.getString("noticeTitle")
                        val date = subJsonObject.getString("date")
                        val content = subJsonObject.getString("content")

                        // 공지사항 리스트에 정보 저장
                        noticeAdapter?.addNoticeList(NoticeItem(noticeTitle, date, content))
                    }
                } catch (e: JSONException) {
                    Log.w("NoticeFragment - Get Notices", e.stackTraceToString())
                    showToast(resources.getString(R.string.toast_server_error)) // 실패
                }
            }
        }
    }

    // 공지사항 조회 실패
    override fun onGetNoticeFailure(status: Int, message: String) {
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