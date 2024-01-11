package com.example.oops_android.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.TimeZone

// 캘린더 함수와 관련한 함수 및 변수 모음
class CalendarUtils {
    companion object {

        // 오늘 날짜 구하기
        fun getTodayDate(): ZonedDateTime =
            Instant.now().atZone(TimeZone.getDefault().toZoneId())

        // 사용자의 기기에 따른 오늘 날짜 구하기
        fun getTodayDateList(): Array<Any?> {

            // 오늘 날짜 기준으로
            val todayList = getTodayDate().toString().split("T") // 년도-월-일만 분리
            val todayFormatList = getTodayDate().toString().split(".") // LocalDateTime 포맷으로 분리
            val localDateTime: LocalDateTime =
                LocalDateTime.parse(todayFormatList[0]) // LocalDateTime으로 만들기
            val weekDate: LocalDateTime? =
                localDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)) // 일주일 시작 날짜 구하기

            return arrayOf(todayList[0], weekDate)

        }
    }
}