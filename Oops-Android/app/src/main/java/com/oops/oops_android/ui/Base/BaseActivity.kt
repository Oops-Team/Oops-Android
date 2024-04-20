package com.oops.oops_android.ui.Base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.oops.oops_android.R
import com.oops.oops_android.databinding.SnackbarBgBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

abstract class BaseActivity<T: ViewBinding>(private val inflate: (LayoutInflater) -> T) : AppCompatActivity() {
    private var mBinding: T? = null
    protected val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()
        super.onCreate(savedInstanceState)

        mBinding = inflate(layoutInflater)
        setContentView(binding.root)

        // 상태바 색상
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.White)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // FCM SDK 초기화
        FirebaseApp.initializeApp(this)

        initAfterBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBinding != null) {
            mBinding = null
        }
    }

    // 권한 체크 리스너
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        // 알림 수신 동의 했다면
        if (isGranted) {
            // 토큰 발급 및 회원가입 API 연동
            getFCMToken()
        }
        // 알림 수신 미동의 했다면
        else {
            connectOopsAPI(null, null)
        }
    }

    // 알림 수신 동의 여부 선택 팝업창 띄우기
    fun askNotificationPermission() {
        // 안드로이드13 이상이라면
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

            // notification설정이 안 되어 있다면, 권한 체크 알림창 띄우기
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            // 설정이 되어 있다면
            else {
                getFCMToken()
            }
        }
    }

    // 현재 등록된 토큰 가져오기 및 API 연결
    fun getFCMToken(loginId: String? = null) {
        // 현재 등록된 토큰 가져오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            // 토큰 가져오기를 실패했을 경우
            if (!task.isSuccessful) {
                Log.w("BaseActivity", "Fetching FCM registration token failed", task.exception)
                connectOopsAPI(null, loginId)
                return@OnCompleteListener
            }

            // FCM 토큰 가져오기
            val token = task.result
            connectOopsAPI(token, loginId)
        })
    }

    // 각 API 연동
    protected abstract fun connectOopsAPI(token: String?, loginId: String?)

    protected abstract fun beforeSetContentView()

    protected abstract fun initAfterBinding()

    // activity 이동
    fun startNextActivity(activity: Class<*>?) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    // activity 이동시 별도 설정
    fun startActivityWithClear(activity: Class<*>?) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    // fragment 이동
    fun startNextFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment, tag)
            .commitNowAllowingStateLoss()
    }

    // 키보드 숨기기
    fun getHideKeyboard(view: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // 키보드 띄우기
    fun getShowKeyboard(edt: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        edt.requestFocus()
        imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }

    // 토스트 메시지 띄우기
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // 커스텀 스낵바 띄우기
    @SuppressLint("ShowToast")
    fun showCustomSnackBar(message: Int) {
        // 스낵바 텍스트 설정
        val snackBarBinding = SnackbarBgBinding.inflate(layoutInflater)
        snackBarBinding.tvSnackbar.text = getString(message)

        // 스낵바 객체 생성
        val snackBar = Snackbar.make(binding.root, getString(message), Snackbar.LENGTH_SHORT)
        snackBar.animationMode = Snackbar.ANIMATION_MODE_FADE

        // 커스텀한 배경 적용
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(snackBarBinding.root)

        // 기존 스낵바 정보 안 보이게 만들기
        val defaultTv = snackBarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        defaultTv.visibility = View.INVISIBLE
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        snackBar.show() // 스낵바 띄우기
    }
}