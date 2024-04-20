package com.oops.oops_android.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.oops.oops_android.config.AlarmFunctions
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.data.db.Entity.RemindAlarm
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/* 알람 등록 및 삭제 함수 */
object AlarmUtils {
    private var alarmContent = "외출 준비를 시작해 볼까요?" // 외출 시간 임박 알림 내용

    // 외출 시간 알림 등록(room db)
    @SuppressLint("SimpleDateFormat")
    fun setAllAlarm(context: Context, selectDate: LocalDate, hour: Int, minute: Int, remindList: ArrayList<Int>) {
        try {
            // 알람 등록하기
            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val hasPermission: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms()
            } else {
                true
            }

            // 알림 권한이 있다면
            if (hasPermission) {
                val tempGoOutTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                val tempGoOutDateTime = "$selectDate " + // 날짜
                        tempGoOutTime + // 시, 분
                        ":00" // 초

                // 외출 시간
                val dateFormat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                val dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val tempDateTime = LocalDateTime.parse(tempGoOutDateTime, dateFormat2)

                // 알림 요청 코드 및 알람 날짜
                //val code = "${selectDate.year}${selectDate.monthValue}${selectDate.dayOfMonth}${binding.timepickerTodo.hour}${binding.timepickerTodo.minute}"
                val alarmDate = "${selectDate.year}${selectDate.monthValue}${selectDate.dayOfMonth}"

                val userDB = AppDatabase.getUserDB()!! // room db의 user db
                val alarmCodeDate = userDB.alarmDao().getAlarmWitDate(alarmDate)

                try {
                    // 기존에 설정된 해당날짜의 알람이 있다면
                    if (alarmCodeDate.isNotEmpty()) {
                        for (element in alarmCodeDate) {
                            cancelAlarm(context, element.alarmCode) // 알람 취소
                        }
                        userDB.alarmDao().deleteAlarmWithDate(alarmDate) // db에서 삭제
                    }
                } catch (e: Exception) {
                    Log.e("TodoFrm - alarm", "의도된 오류")
                }

                for (i in 0 until remindList.size) {
                    // 새로운 값 저장
                    // 5분 전
                    if (remindList[i] == 2) {
                        val tempRemindTime = LocalDateTime.parse(tempDateTime.minusMinutes(5).toString(), dateFormat1)

                        // 포맷 변경
                        val newRemindTime = tempRemindTime.format(dateFormat2)
                        val newAlarmCode = "${alarmDate}2".toInt()

                        setAlarm(context, newRemindTime.toString(), newAlarmCode) // 알람 등록
                        userDB.alarmDao().addAlarm( // db에 알람 추가
                            RemindAlarm(
                            newAlarmCode,
                            alarmDate,
                            newRemindTime.toString(),
                            alarmContent)
                        )
                    }
                    // 30분 전
                    else if (remindList[i] == 3) {
                        val tempRemindTime = LocalDateTime.parse(tempDateTime.minusMinutes(30).toString(), dateFormat1)

                        // 포맷 변경
                        val newRemindTime = tempRemindTime.format(dateFormat2)
                        val newAlarmCode = "${alarmDate}3".toInt()

                        setAlarm(context, newRemindTime.toString(), newAlarmCode) // 알람 등록
                        userDB.alarmDao().addAlarm( // db에 알람 추가
                            RemindAlarm(
                            newAlarmCode,
                            alarmDate,
                            newRemindTime.toString(),
                            alarmContent)
                        )
                    }
                    // 1시간 전
                    else if (remindList[i] == 4) {
                        val tempRemindTime = LocalDateTime.parse(tempDateTime.minusHours(1).toString(), dateFormat1)

                        // 포맷 변경
                        val newRemindTime = tempRemindTime.format(dateFormat2)
                        val newAlarmCode = "${alarmDate}4".toInt()

                        setAlarm(context, newRemindTime.toString(), newAlarmCode) // 알람 등록
                        userDB.alarmDao().addAlarm( // db에 알람 추가
                            RemindAlarm(
                            newAlarmCode,
                            alarmDate,
                            newRemindTime.toString(),
                            alarmContent)
                        )
                    }
                    // 하루 전
                    else if (remindList[i] == 5) {
                        val tempRemindTime = LocalDateTime.parse(tempDateTime.minusDays(1).toString(), dateFormat1)

                        // 포맷 변경
                        val newRemindTime = tempRemindTime.format(dateFormat2)
                        val newAlarmCode = "${alarmDate}5".toInt()

                        setAlarm(context, newRemindTime.toString(), newAlarmCode) // 알람 등록
                        userDB.alarmDao().addAlarm( // db에 알람 추가
                            RemindAlarm(
                            newAlarmCode,
                            alarmDate,
                            newRemindTime.toString(),
                            alarmContent)
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AlarmUtils - set alarm", e.stackTraceToString())
        }
    }

    // 외출 시간 알림 등록(앱에 저장)
    private fun setAlarm(context: Context, time: String, alarmCode: Int, content: String? = null) {
        AlarmFunctions(context).callAlarm(time, alarmCode, "외출 준비를 시작해 볼까요?")
    }

    // 외출 시간 알림 취소(room db)
    fun setCancelAlarm(context: Context, selectDate: LocalDate) {
        try {
            // db에서 알람 삭제하기
            val userDB = AppDatabase.getUserDB()!!
            val alarmDate = "${selectDate.year}${selectDate.monthValue}${selectDate.dayOfMonth}"
            val alarmCodeDate = userDB.alarmDao().getAlarmWitDate(alarmDate)
            for (element in alarmCodeDate) {
                userDB.alarmDao().deleteAlarmWithCode(element.alarmCode) // db에서 알람 삭제
                cancelAlarm(context, element.alarmCode) // 알람 취소하기
            }
        } catch (e: Exception) {
            Log.e("AlarmUtils - cancel alarm", e.stackTraceToString())
        }
    }

    // 외출 시간 알림 모두 취소(room db)
    fun setCancelAllAlarm(context: Context) {
        try {
            val userDB = AppDatabase.getUserDB()!!
            val alarmList = userDB.alarmDao().getAllAlarms()
            for (alarm in alarmList) {
                cancelAlarm(context, alarm.alarmCode) // 알람 취소하기
            }
            // db에서 모든 알람 삭제
            userDB.alarmDao().deleteAllAlarm()
        } catch (e: Exception) {
            Log.e("AlarmUtils - cancel alarm", e.stackTraceToString())
        }
    }

    // 외출 시간 알림 취소(앱에 저장)
    private fun cancelAlarm(context: Context, alarmCode: Int) {
        AlarmFunctions(context).cancelAlarm(alarmCode)
    }
}