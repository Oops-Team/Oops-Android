package com.oops.oops_android.ui.Login

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import com.oops.oops_android.R
import com.oops.oops_android.utils.CalendarUtils

// 앱 푸시 알림 미동의 팝업(알림 수신에 미동의했을 경우 띄워짐)
class PushAlertDisagreeDialog(private val context: Context) {
    fun showDisagreeDialog() {
        // dialog 설정 및 띄우기
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 모서리 둥글게
        dialog.setContentView(R.layout.dialog_push_alert_disagree)
        dialog.setCancelable(false) // 화면 바깥 클릭 시 dialog 닫지 않기
        dialog.show()

        // 날짜 정보 넣기
        val dateList = CalendarUtils.getToday()[0]
        val timeList = CalendarUtils.getToday()[1]
        val today = dateList[0] + "년 " + dateList[1] + "월 " + dateList[2] + "일 " +
                timeList[0] + "시 " + timeList[1] + "분"
        dialog.findViewById<TextView>(R.id.tv_popup_push_alert_disagree_date).text = today

        // 확인 버튼을 누른 경우
        dialog.findViewById<Button>(R.id.btn_popup_push_alert_disagree_confirm).setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    interface DisAgreeButtonClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: DisAgreeButtonClickListener

    fun setOnClickedListener(listener: DisAgreeButtonClickListener) {
        onClickListener = listener
    }
}