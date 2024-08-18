package com.oops.oops_android.ui.Main.Home

import android.os.Handler
import android.os.Parcel
import android.os.Parcelable

/* 홈 화면 & 인벤토리 화면의 챙겨야 할 것 아이템 */
data class StuffItem(
    var stuffImgUrl: String, // 소지품 이미지
    var stuffName: String, // 소지품 이름
    var date: String? = null, // 날짜
    var isTakeStuff: Boolean? = null, // 챙김 여부(0: 안 챙김, 1: 챙김)
    var lastClickTime: Long = 0, // 소지품 마지막 클릭 시간
    var handler: Handler? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readLong(),
        parcel.readValue(Handler::class.java.classLoader) as? Handler
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(stuffImgUrl)
        parcel.writeString(stuffName)
        parcel.writeString(date)
        parcel.writeValue(isTakeStuff)
        parcel.writeLong(lastClickTime)
        parcel.writeValue(handler)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StuffItem> {
        override fun createFromParcel(parcel: Parcel): StuffItem {
            return StuffItem(parcel)
        }

        override fun newArray(size: Int): Array<StuffItem?> {
            return arrayOfNulls(size)
        }
    }
}
