package com.example.oops_android.ui.Main.Inventory

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentInventoryBinding
import com.example.oops_android.ui.Base.BaseFragment
import com.example.oops_android.ui.Main.Home.StuffItem

class InventoryFragment: BaseFragment<FragmentInventoryBinding>(FragmentInventoryBinding::inflate) {
    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Main_500) // 상단 상태바 색상
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.White) // 상단 상태바 색상 변경
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = true
    }

    override fun initAfterBinding() {
        // 카테고리 적용
        val categoryAdapter = InventoryCategoryListAdapter(requireContext())
        categoryAdapter.addCategoryList(CategoryItemUI(-1L, 0L, "ALL", true)) // default값
        categoryAdapter.addCategoryList(CategoryItemUI(0L, 1L, "학교갑시다"))
        categoryAdapter.addCategoryList(CategoryItemUI(1L, 2L, "지독한 현생"))
        categoryAdapter.addCategoryList(CategoryItemUI(2L, 3L, "독서 중"))
        binding.rvInventoryCategory.adapter = categoryAdapter

        // TODO: 카테고리가 5개라면 create 버튼 숨기기
        if (categoryAdapter.itemCount == 5) {
            binding.btnInventoryCreate.visibility = View.INVISIBLE
        }
        else {
            binding.btnInventoryCreate.visibility = View.VISIBLE
        }

        // 카테고리 버튼 클릭 이벤트
        categoryAdapter.onCategoryItemClickListener = { position ->
            val inventoryIdx: Long = categoryAdapter.setCategorySelected(position)

            // 인벤토리가 ALL이라면
            if (inventoryIdx == -1L) {
                binding.tvInventoryStuffEdit.visibility = View.INVISIBLE // edit 아이콘 숨기기
            }
            else {
                binding.tvInventoryStuffEdit.visibility = View.VISIBLE
            }

            // TODO: API 연동 (inventoryIdx 전달)
        }

        // Create 버튼 클릭 이벤트
        binding.btnInventoryCreate.setOnClickListener {
            // TODO: 인벤토리 생성 화면으로 이동
        }

        // 소지품이 없을 경우, 소지품 추가 버튼 클릭 이벤트
        binding.lLayoutInventoryStuffDefault.setOnClickListener {
            // TODO: 소지품 추가 화면으로 이동
        }

        // 인벤토리 All 아이템 출력
        val stuffAdapter = InventoryStuffListAdapter(requireContext())
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_dongle_img, "동글이"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_computer_img, "노트북"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_charger_img, "충전기"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_umbrella_img, "우산"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_wallet_img, "지갑"))
        binding.rvInventoryStuff.adapter = stuffAdapter

        binding.rvInventoryStuff.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener() {
            val width = binding.rvInventoryStuff.width / 2
        })

        // 소지품 아이템이 있다면
        if (stuffAdapter.itemCount >= 1) {
            binding.lLayoutInventoryStuffDefault.visibility = View.GONE // default 뷰 숨기기
            binding.tvInventoryStuffNum.visibility = View.VISIBLE
        }


    }
}