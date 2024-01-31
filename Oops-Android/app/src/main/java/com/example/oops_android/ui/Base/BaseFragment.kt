package com.example.oops_android.ui.Base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.oops_android.R
import com.example.oops_android.databinding.SnackbarBgBinding
import com.example.oops_android.ui.Main.MainActivity
import com.example.oops_android.utils.Inflate
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB: ViewBinding>(private val inflate: Inflate<VB>): Fragment() {
    private var mBinding: VB? = null
    protected val binding get() = mBinding!!

    protected var mainActivity: MainActivity? = null // 메인액티비티 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.White)
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = true
        initViewCreated()
    }

    protected abstract fun initViewCreated()

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    protected abstract fun initAfterBinding()

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    // 토스트 메시지 띄우기
    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    // 키보드 숨기기
    fun getHideKeyboard(view: View) {
        mainActivity!!.getHideKeyboard(view)
    }

    // 키보드 띄우기
    fun getShowKeyboard(edt: EditText) {
        mainActivity!!.getShowKeyboard(edt)
    }

    // 툴 바 제목 설정
    fun setToolbarTitle(toolbar: TextView, title: String) {
        toolbar.text = title
    }
}