package com.oops.oops_android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

// 공통적으로 사용하는 데이터를 관리하는 파일
class ApplicationClass: Application() {
    init {
        instance = this
    }
    companion object {
        lateinit var instance: ApplicationClass

        // 전용 applicationcontext 가져오기
        fun applicationContext(): Context {
            return instance.applicationContext
        }

        const val TAG: String = "AUTH" // SharedPreferences 키 값
        lateinit var mSharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}