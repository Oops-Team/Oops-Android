package com.oops.oops_android.ui.Main.Inventory

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentInventoryBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Main.Home.StuffItem
import java.lang.Exception

/* 인벤토리 화면 */
class InventoryFragment: BaseFragment<FragmentInventoryBinding>(FragmentInventoryBinding::inflate) {

    private lateinit var categoryAdapter: InventoryCategoryListAdapter // 인벤토리 카테고리 어댑터

    override fun initViewCreated() {
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Main_500) // 상단 상태바 색상
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = false

        // 바텀 네비게이션 보이기
        mainActivity?.hideBnv(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.White) // 상단 상태바 색상 변경
        WindowInsetsControllerCompat(mainActivity!!.window, mainActivity!!.window.decorView).isAppearanceLightStatusBars = true
    }

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {
        // 카테고리 적용
        categoryAdapter = InventoryCategoryListAdapter(requireContext())
        var tempList1 = CategoryItemUI(-1L, 0, "ALL", true) // default값
        var tempList2 = CategoryItemUI(0L, 1, "학교갑시다", false, arrayListOf(1, 2, 3))
        var tempList3 = CategoryItemUI(1L, 2, "지독한현생", false, arrayListOf(4, 5))
        var tempList4 = CategoryItemUI(2L, 3, "독서 시간", false, arrayListOf(6))

        val categoryList = CategoryList()
        categoryList.add(tempList1)
        categoryList.add(tempList2)
        categoryList.add(tempList3)
        categoryList.add(tempList4)

        // 인벤토리 생성&수정 화면에서 넘어왔다면
        try {
            val args: InventoryFragmentArgs by navArgs()
            val categoryItem = args.newInventoryItem

            when (args.inventoryDivision) {
                // 인벤토리 삭제의 경우
                "InventoryDelete" -> {
                    // 인벤토리 idx가 같은 아이템 찾기
                    for (i in 0 until categoryList.size) {
                        if (categoryList[i].inventoryIdx == categoryItem?.inventoryIdx) {
                            // 값 삭제
                            categoryList.removeAt(i)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("Inventory Fragment Error", e.message.toString())
        }

        categoryAdapter.addCategoryList(categoryList)
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
            categoryAdapter.setCategorySelected(position)

            // 인벤토리가 ALL이라면
            val inventoryIdx: Long = categoryAdapter.getCategoryIdx(position)
            if (inventoryIdx == -1L) {
                binding.ivInventoryStuffEdit.visibility = View.INVISIBLE // edit 아이콘 숨기기
            }
            else {
                binding.ivInventoryStuffEdit.visibility = View.VISIBLE
            }

            // TODO: API 연동 (inventoryIdx 전달)
        }

        // 인벤토리 아이콘 클릭 이벤트
        categoryAdapter.onCategoryIconClickListener = { position, iconImg ->
            val categoryItem = categoryAdapter.getCategoryItem(position)

            // 현재 선택 중인 카테고리이며, all 카테고리가 아니라면
            if (categoryItem.isSelected && categoryItem.inventoryIdx != -1L) {
                // 아이콘 변경 팝업 띄우기
                showEditIconPopup(position, iconImg)
            }
        }

        // Create 버튼 클릭 이벤트
        binding.btnInventoryCreate.setOnClickListener {
            // 전체 카테고리 정보 가져오기
            val tempCategoryList: CategoryList = categoryAdapter.getCategoryItemList()

            // 인벤토리 생성 화면으로 이동
            val actionToCreateInventory: NavDirections = InventoryFragmentDirections.actionInventoryFrmToCreateInventoryFrm(
                "InventoryCreate",
                tempCategoryList,
                null
            )
            findNavController().navigate(actionToCreateInventory)
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
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_handcream_img, "핸드크림"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_sunglasses_img, "선글라스"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_alcohol_swap_img, "알콜스왑"))
        stuffAdapter.addStuffList(StuffItem(R.drawable.stuff_mouse_img, "마우스"))
        binding.rvInventoryStuff.adapter = stuffAdapter

        // 소지품 아이템이 있다면
        if (stuffAdapter.itemCount >= 1) {
            binding.lLayoutInventoryStuffDefault.visibility = View.GONE // default 뷰 숨기기
            binding.tvInventoryStuffNum.visibility = View.VISIBLE
            binding.tvInventoryStuffNum.text = stuffAdapter.itemCount.toString() + "/77"
        }

        // 소지품 수정 버튼 클릭 이벤트
        binding.ivInventoryStuffEdit.setOnClickListener {
            // 현재 선택 중인 인벤토리 정보 가져오기
            val inventoryItem: CategoryItemUI? = categoryAdapter.getSelectedCategoryItem()

            // 전체 카테고리 정보 가져오기
            val tempCategoryList: CategoryList = categoryAdapter.getCategoryItemList()

            // 인벤토리 수정 화면으로 이동
            val actionToCreateInventory: NavDirections = InventoryFragmentDirections.actionInventoryFrmToCreateInventoryFrm(
                "InventoryEdit",
                tempCategoryList,
                inventoryItem
            )
            findNavController().navigate(actionToCreateInventory)
        }
    }

    // 인벤토리의 아이콘 변경 팝업 띄우기
    private fun showEditIconPopup(position: Int, iconImg: ImageView) {
        val popup = layoutInflater.inflate(R.layout.layout_inventory_bubble, null)

        // popup window 생성
        val popupWindow = PopupWindow(popup, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true // 팝업 바깥 영역 클릭 시 팝업 닫기
        popupWindow.showAsDropDown(iconImg, -10, 40)

        /* 아이콘 클릭 이벤트 */
        // 시간 아이콘 클릭 이벤트
        val timeIBtn: ImageButton = popup.findViewById(R.id.iBtn_inventory_bubble_time)
        timeIBtn.setOnClickListener {

            // 아이콘이 변경됐다면
            if (changeInventoryIcon(position, 1)) {
                // TODO: API 연동해서 값 바꾸기
                showToast("$position 시간 아이콘으로 변경")
            }
            popupWindow.dismiss() // 팝업 닫기
        }

        // 달리기 아이콘 클릭 이벤트
        val runIBtn: ImageButton = popup.findViewById(R.id.iBtn_inventory_bubble_run)
        runIBtn.setOnClickListener {

            // 아이콘이 변경됐다면
            if (changeInventoryIcon(position, 2)) {
                // TODO: API 연동해서 값 바꾸기
                showToast("$position 달리기 아이콘으로 변경")
            }
            popupWindow.dismiss() // 팝업 닫기
        }

        // 지갑 아이콘 클릭 이벤트
        val walletIBtn: ImageButton = popup.findViewById(R.id.iBtn_inventory_bubble_wallet)
        walletIBtn.setOnClickListener {

            // 아이콘이 변경됐다면
            if (changeInventoryIcon(position, 3)) {
                // TODO: API 연동해서 값 바꾸기
                showToast("$position 지갑 아이콘으로 변경")
            }
            popupWindow.dismiss() // 팝업 닫기
        }

        // 컴퓨터 아이콘 클릭 이벤트
        val computerIBtn: ImageButton = popup.findViewById(R.id.iBtn_inventory_bubble_computer)
        computerIBtn.setOnClickListener {

            // 아이콘이 변경됐다면
            if (changeInventoryIcon(position, 4)) {
                // TODO: API 연동해서 값 바꾸기
                showToast("$position 컴퓨터 아이콘으로 변경")
            }
            popupWindow.dismiss() // 팝업 닫기
        }

        // 톱니바퀴 아이콘 클릭 이벤트
        val wheelIBtn: ImageButton = popup.findViewById(R.id.iBtn_inventory_bubble_wheel)
        wheelIBtn.setOnClickListener {

            // 아이콘이 변경됐다면
            if (changeInventoryIcon(position, 5)) {
                // TODO: API 연동해서 값 바꾸기
                showToast("$position 톱니바퀴 아이콘으로 변경")
            }
            popupWindow.dismiss() // 팝업 닫기
        }
    }

    // 인벤토리의 아이콘 변경 로직 함수
    private fun changeInventoryIcon(position: Int, inventoryIconIdx: Int): Boolean {
        // 아이콘 변경
        return categoryAdapter.modifyCategoryItem(position, inventoryIconIdx)
    }
}