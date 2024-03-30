package com.oops.oops_android.ui.Main.Sting

// 모든 친구 목록 화면에서 사용되는 아이템
// 프로필 이미지, 닉네임, 수락or거절, 대기중, 친구신청
data class FriendsItem (
    var userIdx: Long, // 친구 PK
    var userName: String, // 친구 닉네임
    var userImg: String, // 친구 사진
    var userState: Int // 친구 상태(친구x: 0, 친구o: 1, 대기중: 2, 친구요청이 들어온 경우: 3)
)

// 친구 신청 성공 시 뷰에 전달할 모델
data class StingRequestModel(
    var name: String, // 친구 이름
    var position: Int // rv내의 position
)

// 친구 수락 성공 시 뷰에 전달할 모델
data class StingAcceptModel(
    var position: Int, // rv내의 positin
    var newFriend: FriendsItem // 수락 성공한 친구
)

// 친구 끊기 & 거절 성공 시 뷰에 전달할 모델
data class StingRefuseModel(
    var isRefuse: Boolean, // 친구 거절: true, 친구 끊기: false -> 팝업창 띄워주기 로직을 위함
    var position: Int, // rv내의 position
    var newFriend: FriendsItem // 끊기&거절한 친구
)