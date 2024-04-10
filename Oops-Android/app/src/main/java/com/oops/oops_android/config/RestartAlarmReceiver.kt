package com.oops.oops_android.config

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.oops.oops_android.data.db.Database.AppDatabase

/* 기기 재부팅 시 취소되었던 알람 재설정 */
class RestartAlarmReceiver: BroadcastReceiver() {
    // 알람 재설정
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            // 알람 등록하기
            val functions = AlarmFunctions(context!!)
            val db = AppDatabase.getUserDB()!!
            val alarmList = db.alarmDao().getAllAlarms()
            val alarmListSize = db.alarmDao().getAllAlarms().size
            alarmList.let {
                for (i in 0 until alarmListSize) {
                    val time = alarmList[i].time
                    val code = alarmList[i].alarmCode
                    val content = alarmList[i].content
                    functions.callAlarm(time, code, content)
                }
            }
        }
    }
}