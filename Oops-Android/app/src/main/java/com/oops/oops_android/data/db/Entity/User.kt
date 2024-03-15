package com.oops.oops_android.data.db.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* Room DB에서 사용하는 User Data Model */
@Entity(tableName = "UserTable")
data class User(
    @ColumnInfo(name ="loginId") val loginId: String, // 로그인 유형
    @ColumnInfo(name ="name") val name: String? = null // 닉네임
) {
    @PrimaryKey(autoGenerate = true) var uId: Int = 0
}