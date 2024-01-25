package com.example.oops_android.ui.Main.Home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import com.example.oops_android.databinding.DialogEditBinding
import com.example.oops_android.ui.Main.MainActivity

// 홈 화면의 수정 버튼을 누르면 보이는 다이얼로그
class EditDialog(
    private val context: Context,
    private val mainActivity: MainActivity) {
    private var mBinding: DialogEditBinding? = null
    private val binding get() = mBinding!!

    // dialog 띄우기
    fun editDialogShow() {
        mBinding = DialogEditBinding.inflate(mainActivity.layoutInflater)

        // dialog 설정
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
        dialog.window?.setDimAmount(0.7f) // 배경 투명도 30%
        dialog.setContentView(binding.root) // xml 적용
        dialog.setCancelable(true) // dialog의 바깥 화면 클릭 시 dialog 닫히도록 함
        dialog.show()

        // TODO:: blur 효과 적용x..
        dialog.window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setBackgroundBlurRadius(30)
                attributes!!.blurBehindRadius = 30
            }
        }

        // 소지품 수정 버튼을 누른 경우
        binding.dialogEditStuff.setOnClickListener {
            dialog.dismiss() // dialog 숨기기
        }
        // 오늘 할 일 수정 버튼을 누른 경우
        binding.dialogEditTodo.setOnClickListener {
            dialog.dismiss() // dialog 숨기기
        }
    }
    interface ButtonClickListener {
        fun onClicked()
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }
}