package com.example.oops_android.ui.Main.Inventory

import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentInventoryBinding
import com.example.oops_android.ui.Base.BaseFragment

class InventoryFragment: BaseFragment<FragmentInventoryBinding>(FragmentInventoryBinding::inflate) {
    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Main_500) // 상단 상태바 색상
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = false
    }

    override fun initAfterBinding() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.White) // 상단 상태바 색상 변경
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = true
    }
}