package com.oops.oops_android.data.remote.Sting.Model

import com.google.gson.annotations.SerializedName

/* 친구 화면에서 사용하는 데이터 모델 */
// 콕콕 찌르기, 친구 신청
data class StingFriendModel(
    @SerializedName("name") val name: String // 찌를 친구의 닉네임, 친구 신청 대상 닉네임
)

// 친구 신청 수락
data class StingFriendIdModel(
    @SerializedName("friendId") val friendId: Long // 친구 수락 대상 아이디
)
