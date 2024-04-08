package com.oops.oops_android.data.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.oops.oops_android.data.db.Entity.RemindAlarm

/* 핸드폰 재부팅 시 관리되어야 하는 알람 테이블 */
@Dao
interface RemindAlarmDao {
    // 모든 알람 정보 가져오기
    @Query("SELECT * FROM RemindAlarms")
    fun getAllAlarms(): List<RemindAlarm>

    // 알람 추가
    @Insert
    fun addAlarm(alarm: RemindAlarm)

    // 알람 날짜로 삭제
    @Query("DELETE FROM RemindAlarms WHERE alarmDate = :alarmDate")
    fun deleteAlarmWithDate(alarmDate: String)

    // 알람 코드로 삭제
    @Query("DELETE FROM RemindAlarms WHERE alarmCode = :alarmCode")
    fun deleteAlarmWithCode(alarmCode: Int)

    // 알람 날짜로 알람 정보 검색
    @Query("SELECT * FROM RemindAlarms WHERE alarmDate = :alarmDate")
    fun getAlarmWitDate(alarmDate: String): List<RemindAlarm>

    // 모든 알람 삭제
    @Query("DELETE FROM RemindAlarms")
    fun deleteAllAlarm()
}