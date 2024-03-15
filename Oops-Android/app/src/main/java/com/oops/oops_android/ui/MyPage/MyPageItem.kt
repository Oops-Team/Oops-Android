package com.oops.oops_android.ui.MyPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/* 마이 페이지 화면에서 사용되는 데이터 클래스 */
@Parcelize
data class WithdrawalItem(
    var reason1: Boolean = false, // 사유1
    var reason2: Boolean = false, // 사유2
    var reason3: Boolean = false, // 사유3
    var reason4: Boolean = false, // 사유4
    var reason5: Boolean = false, // 사유5
    var subReason: String? = null // 기타
): Parcelable