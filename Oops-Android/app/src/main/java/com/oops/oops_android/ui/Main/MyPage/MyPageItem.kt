package com.oops.oops_android.ui.Main.MyPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/* 마이 페이지 화면에서 사용되는 데이터 클래스 */
// 사용자 정보
@Parcelize
data class MyPageItem(
    val loginType: String, // 로그인 유형
    val userEmail: String, // 이메일
    val userName: String, // 사용자 이름
    val isPublic: Boolean // 프로필 공개 여부
): Parcelable

// 회원 탈퇴
@Parcelize
data class WithdrawalItem(
    var reason1: Boolean = false, // 사유1
    var reason2: Boolean = false, // 사유2
    var reason3: Boolean = false, // 사유3
    var reason4: Boolean = false, // 사유4
    var reason5: Boolean = false, // 사유5
    var subReason: String? = null // 기타
): Parcelable