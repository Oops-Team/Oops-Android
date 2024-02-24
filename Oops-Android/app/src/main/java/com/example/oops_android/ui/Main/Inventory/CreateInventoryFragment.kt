package com.example.oops_android.ui.Main.Inventory

import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.oops_android.R
import com.example.oops_android.databinding.FragmentCreateInventoryBinding
import com.example.oops_android.ui.Base.BaseFragment

/* 인벤토리 생성 & 수정 화면 */
class CreateInventoryFragment: BaseFragment<FragmentCreateInventoryBinding>(FragmentCreateInventoryBinding::inflate) {
    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        val args: CreateInventoryFragmentArgs by navArgs()
        when (args.inventoryDivision) {
            // 툴 바 제목 설정 및 버튼 띄우기
            "InventoryCreate" -> {
                setToolbarTitle(binding.toolbarCreateInventory.tvSubToolbarTitle, "인벤토리 생성")
                binding.cvCreateInventoryStuffAdd.visibility = View.VISIBLE
            }
            "InventoryEdit" -> {
                setToolbarTitle(binding.toolbarCreateInventory.tvSubToolbarTitle, "인벤토리 수정")
                binding.cvCreateInventoryStuffAdd.visibility = View.GONE
                binding.lLayoutCreateInventoryEdit.visibility = View.VISIBLE
                binding.toolbarCreateInventory.ivSubToolbarRight.visibility = View.VISIBLE
            }
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭
        binding.toolbarCreateInventory.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }
}