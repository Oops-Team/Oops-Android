package com.oops.oops_android.ui.Main.Home

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/* 홈 화면에서 사용하는 인벤토리 리스트 */
data class HomeInventoryItem(
    var inventoryId: Long, // 인벤토리 idx
    var inventoryName: String, // 인벤토리 이름
    var inventoryIconIdx: Int, // 인벤토리 아이콘
    var isInventoryUsed: Boolean // 현재 사용 중인 인벤토리
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(inventoryId)
        parcel.writeString(inventoryName)
        parcel.writeInt(inventoryIconIdx)
        parcel.writeByte(if (isInventoryUsed) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeInventoryItem> {
        override fun createFromParcel(parcel: Parcel): HomeInventoryItem {
            return HomeInventoryItem(parcel)
        }

        override fun newArray(size: Int): Array<HomeInventoryItem?> {
            return arrayOfNulls(size)
        }
    }
}