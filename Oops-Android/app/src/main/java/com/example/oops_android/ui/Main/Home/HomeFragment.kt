package com.example.oops_android.ui.Main.Home

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentHomeBinding
import com.example.oops_android.ui.BaseFragment

class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    // 뒤로가기 콜백
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 핸드폰 뒤로가기 이벤트
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.getBackPressedEvent()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove() // 콜백 제거
    }

    override fun initViewCreated() {
    }

    override fun initAfterBinding() {
    }

}