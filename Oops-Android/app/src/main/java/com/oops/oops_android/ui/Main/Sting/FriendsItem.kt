package com.oops.oops_android.ui.Main.Sting

// 모든 친구 목록 화면에서 사용되는 아이템
// 프로필 이미지, 닉네임, 수락or거절, 대기중, 친구신청
class FriendsItem (
    var userIdx: Long, // 친구 PK
    var userName: String, // 친구 닉네임
    var userImg: String, // 친구 사진
    var userState: Long // 친구 상태(친구x: 0, 친구o: 1, 대기중: 2, 친구요청이 들어온 경우: 3)
)
