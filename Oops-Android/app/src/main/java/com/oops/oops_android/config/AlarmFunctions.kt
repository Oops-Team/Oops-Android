package com.oops.oops_android.config

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/* 알람 생성, 취소 */
class AlarmFunctions(private val context: Context) {
    private lateinit var pendingIntent: PendingIntent

    // 알람 울리기
    @SuppressLint("ScheduleExactAlarm", "SimpleDateFormat")
    fun callAlarm(time: String, alarmCode: Int, content: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java) // 리시버로 전달될 인텐트 설정

        receiverIntent.apply {
            putExtra("alarm_rqCode", alarmCode) //요청 코드를 리시버에 전달
            putExtra("content", content) // 일정 내용을 리시버에 전달
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context, alarmCode, receiverIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, alarmCode, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var datetime = Date()
        try {
            datetime = dateFormat.parse(time) as Date
        } catch (e: ParseException) {
            Log.e("AlarmFunctions", e.stackTraceToString())
        }

        val calendar = Calendar.getInstance()
        calendar.time = datetime

        // API 23 이상 시
        // 도즈모드(배터리 절약 정책)에는 알람이 울리지 않음
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
        val alarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClock, pendingIntent)
    }

    // 알람 취소
    fun cancelAlarm(alarmCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context, alarmCode, intent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, alarmCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmManager.cancel(pendingIntent)
    }
}