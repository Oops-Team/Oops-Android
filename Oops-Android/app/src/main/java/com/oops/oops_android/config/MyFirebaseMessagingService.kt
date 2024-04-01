package com.oops.oops_android.config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.oops.oops_android.R
import com.oops.oops_android.ui.Base.BaseActivity
import java.net.URLDecoder


/* 파이어베이스를 사용하여 콕콕 찌르기 및 외출 알림 수신 구현하기 */
class MyFirebaseMessagingService: FirebaseMessagingService() {
    // Log Tag 메시지
    private val MESSAGING_TAG = "MyFirebaseMessagingService"

    /**
     * 새로운 토큰이 발행될 때마다 해당 함수 콜백 호출
     * 등록 토큰이 처음 생성됨
     * 이 곳에서 토큰 검색 가능
     **/
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 토큰을 서버로 전송
        sendRegistrationToServer(token)
    }

    /**
     * 메시지를 수신할 때 호출
     * 수신된 message 객체 기준으로 작업 수행, 메시지 데이터 가져오기 가능
     * 포그라운드
    **/
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(MESSAGING_TAG, message.from.toString())

        // 메시지 로드 여부 처리
        if (message.data.isNotEmpty()) {
            Log.d(MESSAGING_TAG, "Message data: ${message.data}")
            Log.d(MESSAGING_TAG, "Message noti: ${message.notification}")

            sendNotification(
                message.data["title"].toString(),
                message.data["body"].toString()
            )
        } else {
            // 메시지에 알림 페이로드가 포함되어 있는지 확인
            message.notification?.let {
                sendNotification(
                    message.notification!!.title.toString(),
                    message.notification!!.body.toString()
                )
            }
        }
    }

    // 타사 서버에 토큰을 유지
    private fun sendRegistrationToServer(token: String?) {
        Log.d(MESSAGING_TAG, "sendRegistrationToServer($token)")
    }

    // 수신 된 FCM 메시지를 포함하는 간단한 알림을 만들고 표시
    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, BaseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_oops_channel"
        val channelName = "Oops"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setContentTitle(URLDecoder.decode(title, "UTF-8"))
            .setContentText(URLDecoder.decode(body, "UTF-8"))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 앱의 알림 채널을 시스템에 등록
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }
}