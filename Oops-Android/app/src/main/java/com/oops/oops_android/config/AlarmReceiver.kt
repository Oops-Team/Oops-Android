package com.oops.oops_android.config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.oops.oops_android.R
import com.oops.oops_android.data.db.Database.AppDatabase
import com.oops.oops_android.ui.Main.MainActivity

/* 정해진 시간에 AlarmManager로부터 호출을 받는 리시버 */
class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "Oops_alarm_channel"
        const val CHANNEL_NAME = "Oops"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent2 = Intent(context, AlarmService::class.java)
        val requestCode = intent?.extras!!.getInt("alarm_rqCode")
        val content = intent.extras!!.getString("content")

        Log.e("AlarmReceiver is Called", requestCode.toString())

        // 울린 알람 삭제
        val userDB = AppDatabase.getUserDB()!!
        userDB.alarmDao().deleteAlarmWithCode(requestCode)

        /*val serviceIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getService(
                context,
                requestCode,
                intent2,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getService(
                context,
                requestCode,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }*/

        // 홈 화면으로 이동하는 로직
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra("alarm_rqCode", requestCode)
        activityIntent.putExtra("content", content)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                requestCode,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE
            ) // Activity를 시작하는 인텐트 생성
        } else {
            PendingIntent.getActivity(
                context,
                requestCode,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val manager: NotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = builder
            .setContentText(content)
            .setColor(ContextCompat.getColor(context, R.color.Main_500))
            .setSmallIcon(R.drawable.ic_noti_logo)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .build()

        // 앱 알림 채널을 시스템에 등록
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        manager.notify(1, notification)

        // AlarmService 시작
        context.startService(intent2)
    }
}