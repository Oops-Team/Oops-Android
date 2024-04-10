package com.oops.oops_android.data.db.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* 외출 시간 알림 */
@Entity(tableName = "RemindAlarms")
data class RemindAlarm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alarmCode") val alarmCode: Int, // 알람 요청 코드
    @ColumnInfo(name = "alarmDate") val alarmDate: String, // 날짜
    @ColumnInfo(name = "time") val time: String, // 시간
    @ColumnInfo(name = "content") val content: String // 알람 내용
)
