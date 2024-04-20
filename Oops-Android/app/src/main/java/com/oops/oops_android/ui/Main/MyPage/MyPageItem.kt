package com.oops.oops_android.ui.Main.MyPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/* 마이 페이지 화면에서 사용되는 데이터 클래스 */
// 사용자 정보
@Parcelize
data class MyPageItem(
    val userImgURI: String?, // 사용자 프로필 사진
    val loginType: String, // 로그인 유형
    val userEmail: String, // 이메일
    val userName: String, // 사용자 이름
    val isPublic: Boolean, // 프로필 공개 여부
    val isAlert: Boolean // 알림 설정 여부
): Parcelable

// 회원 탈퇴
@Parcelize
data class WithdrawalItem(
    var reasonType: Int, // 탈퇴 사유
    var subReason: String? = null // 기타
): Parcelable

// 공지사항 정보
@Parcelize
data class NoticeItem(
    var noticeTitle: String, // 공지사항 제목
    var date: String, // 게시 날짜
    var content: String // 공지사항 내용
): Parcelable