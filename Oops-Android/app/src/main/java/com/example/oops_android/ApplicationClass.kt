package com.example.oops_android

import android.app.Application
import android.content.Context

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
    }
}