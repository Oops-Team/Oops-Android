package com.oops.oops_android.config

import android.app.Service
import android.content.Intent
import android.os.IBinder

/* AlarmReceiver가 호출할 Service 클래스, 백그라운드에서 알람을 실행 */
class AlarmService: Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }
}