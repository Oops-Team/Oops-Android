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
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Inventory.Api.InventoryService
import com.oops.oops_android.data.remote.Inventory.Api.InventoryView
import com.oops.oops_android.databinding.FragmentInventoryBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Main.Home.StuffItem
import org.json.JSONObject

/* 인벤토리 화면 */
class InventoryFragment: BaseFragment<FragmentInventoryBinding>(FragmentInventoryBinding::inflate), InventoryView {

    private var categoryList = CategoryList() // 인벤토리 생성&수정 화면에 넘겨줄 인벤토리 리스트
    private lateinit var categoryAdapter: InventoryCategoryListAdapter // 인벤토리 카테고리 어댑터

    private var stuffList = ArrayList<StuffItem>() // 각 인벤토리 내의 소지품 리스트
    private var stuffAdapter = InventoryStuffListAdapter(stuffList) // 각 인벤토래 내의 소지품 리스트 어댑터

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

        // 인벤토리 전체 리스트 조회 API 연결
        getAllInventory()

        // 인벤토리 All 소지품 어댑터 연결
        binding.rvInventoryStuff.adapter = stuffAdapter

        // 인벤토리 생성&수정 화면에서 넘어왔다면
        /*try {
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
        }*/

        // 카테고리 버튼 클릭 이벤트
        categoryAdapter.onCategoryItemClickListener = { position ->
            categoryAdapter.setCategorySelected(position)
            val inventoryIdx: Long = categoryAdapter.getCategoryIdx(position)

            // All 인벤토리라면
            if (inventoryIdx == -1L) {
                binding.ivInventoryStuffEdit.visibility = View.INVISIBLE // edit 아이콘 숨기기
                getAllInventory() // 전체 인벤토리 조회 API 연결
            }
            // 상세 인벤토리라면
            else {
                binding.ivInventoryStuffEdit.visibility = View.VISIBLE
                getDetailInventory(inventoryIdx) // 상세 인벤토리 조회 API 연결
            }
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
            // 소지품 추가 화면으로 이동하기
            val actionToStuffAdd: NavDirections = InventoryFragmentDirections.actionInventoryFrmToStuffAddFrm(
                "Inventory",
                null
            )
            findNavController().navigate(actionToStuffAdd)
        }

        // 소지품 아이템이 있다면
        if (stuffAdapter.itemCount >= 1) {
            binding.lLayoutInventoryStuffDefault.visibility = View.GONE // default 뷰 숨기기
            binding.tvInventoryStuffNum.visibility = View.VISIBLE
            binding.tvInventoryStuffNum.text = stuffAdapter.itemCount.toString() + "/80"
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

    // 인벤토리 전체 리스트 조회 API 연결
    private fun getAllInventory() {
        val inventoryService = InventoryService()
        inventoryService.setInventoryView(this)
        inventoryService.getAllInventory()
    }

    // 상세 인벤토리 조회 API 연결
    private fun getDetailInventory(inventoryIdx: Long) {
        val inventoryService = InventoryService()
        inventoryService.setInventoryView(this)
        inventoryService.getDetailInventory(inventoryIdx)
    }

    // 인벤토리 전체 리스트 조회 성공
    @SuppressLint("SetTextI18n")
    override fun onGetInventorySuccess(status: Int, message: String, data: JsonObject?) {
        when (message) {
            // 전체 인벤토리 조회
            "All Inventory" -> {
                val jsonObject = JSONObject(data.toString())

                val inventoryIdx = jsonObject.getJSONArray("inventoryIdx")
                val inventoryIconIdx = jsonObject.getJSONArray("inventoryIconIdx")
                val inventoryName = jsonObject.getJSONArray("inventoryName")

                val stuffNum = jsonObject.getInt("stuffNum")
                binding.tvInventoryStuffNum.text = "$stuffNum/80"

                val allList = CategoryItemUI(-1L, 0, "ALL", true) // default값
                categoryList.clear()
                categoryList.add(allList)

                for (i in 0 until inventoryIconIdx.length()) {
                    categoryList.add(CategoryItemUI(
                        inventoryIdx.getLong(i),
                        inventoryIconIdx.getInt(i),
                        inventoryName.getString(i),
                        false,
                        null)
                    )
                }
                Log.d("categoryList", categoryList.toString() + allList.toString())
                categoryAdapter.addCategoryList(categoryList)
                binding.rvInventoryCategory.adapter = categoryAdapter

                // 카테고리가 5개라면 create 버튼 숨기기
                if (categoryAdapter.itemCount == 5) {
                    binding.btnInventoryCreate.visibility = View.INVISIBLE
                }
                else {
                    binding.btnInventoryCreate.visibility = View.VISIBLE
                }

                val tempStuffList = jsonObject.getJSONArray("stuffList")
                stuffList.clear()
                for (i in 0 until tempStuffList.length()) {
                    val subObject = tempStuffList.getJSONObject(i)
                    val stuffImgUrl = subObject.getString("stuffImgUrl")
                    val stuffName = subObject.getString("stuffName")

                    stuffList.add(StuffItem(stuffImgUrl, stuffName)) // 소지품 추가
                }
            }
            // 상세 인벤토리 조회
            "Detail Inventory" -> {
                val jsonObject = JSONObject(data.toString())

                val inventoryName = jsonObject.getJSONObject("inventoryName").toString()
                val inventoryTag = jsonObject.getJSONArray("inventoryTag")
                val tempTagList = ArrayList<Int>()
                for (i in 0 until inventoryTag.length()) {
                    val tempTag = inventoryTag.getInt(i)
                    tempTagList.add(tempTag)
                }

                // 선택된 상세 인벤토리에 inventoryTag 리스트 넣기
                categoryAdapter.setCategoryTag(categoryAdapter.getCategoryIdx(inventoryName).toInt(), tempTagList)

                val stuffImgURIList = jsonObject.getJSONArray("stuffImgURIList")
                val stuffNameList = jsonObject.getJSONArray("stuffNameList")
                stuffList.clear()
                for (i in 0 until stuffImgURIList.length()) {
                    val tempURI = stuffImgURIList.getString(i)
                    val tempName = stuffNameList.getString(i)

                    stuffList.add(StuffItem(tempURI, tempName, null, null))
                }
            }
        }
    }

    // 인벤토리 전체 리스트 조회 실패
    override fun onGetInventoryFailure(status: Int, message: String) {
        when (status) {
            404 -> showToast(message)
            else -> showToast(resources.getString(R.string.toast_server_error))
        }
    }
}