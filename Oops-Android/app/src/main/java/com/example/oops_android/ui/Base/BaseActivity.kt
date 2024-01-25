package com.example.oops_android.ui.Base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.oops_android.R

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

        initAfterBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBinding != null) {
            mBinding = null
        }
    }

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

    // 토스트 메시지 띄우기
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // 취소 버튼 이벤트
    @SuppressLint("ClickableViewAccessibility")
    fun clickCancelBtn(edt: EditText, alert: TextView) {
        if (edt.text.toString().isNotBlank()) {
            val cancelBtn = ContextCompat.getDrawable(applicationContext, R.drawable.ic_cancel_25)
            edt.setCompoundDrawablesWithIntrinsicBounds(null, null, cancelBtn, null)

            // 취소 버튼 클릭 이벤트
            edt.setOnTouchListener(View.OnTouchListener { _, event ->
                val DRAWABLE_RIGHT = 2

                try {
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= (edt.right -
                                    edt.compoundDrawables[DRAWABLE_RIGHT].bounds.width() - 12)) {
                            // 취소 버튼 삭제 및 입력된 text 삭제
                            edt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                            edt.text = null
                            alert.text = null
                            alert.visibility = View.INVISIBLE
                            return@OnTouchListener true
                        }
                    }
                    false
                } catch (e: Exception) {
                    false
                }
            })
        }
        else
            edt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }
}