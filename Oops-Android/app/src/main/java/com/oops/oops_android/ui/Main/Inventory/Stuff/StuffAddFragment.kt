package com.oops.oops_android.ui.Main.Inventory.Stuff

import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonArray
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Stuff.Api.StuffService
import com.oops.oops_android.data.remote.Stuff.Api.StuffView
import com.oops.oops_android.data.remote.Stuff.Model.StuffAddInventoryModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import com.oops.oops_android.databinding.FragmentStuffAddBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Main.Inventory.InventoryModifyDialog
import com.oops.oops_android.utils.ButtonUtils
import org.json.JSONArray

/* 챙겨야 할 것 추가 & 물품 추가 화면 */
class StuffAddFragment: BaseFragment<FragmentStuffAddBinding>(FragmentStuffAddBinding::inflate), StuffView, CommonView {

    private var inventoryId: Long = 0L // 인벤토리 id
    private var stuffList = ArrayList<StuffAddItem>() // 각 인벤토리 내의 소지품 리스트
    private var stuffAddListAdapter = StuffAddListAdapter(stuffList) // 소지품 리스트 어댑터
    private var previousStuffList = ArrayList<StuffAddItem>() // 수정 이전 소지품 리스트

    private var screenDivision = 0 // 0: add, 1: edit, 2: home
    private var isEnable = false // 완료 버튼 활성화 여부

    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        val args: StuffAddFragmentArgs by navArgs()
        // 툴 바 제목 설정
        when (args.stuffDivision) {
            // 인벤토리 추가 화면에서 넘어 온 경우
            "Inventory Add" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "물품 추가")

                // 데이터 저장하기
                inventoryId = args.inventoryId
                screenDivision = 0
            }
            // 인벤토리 수정 화면에서 넘어 온 경우
            "Inventory Edit" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "물품 추가")

                // 데이터 저장하기
                inventoryId = args.inventoryId
                screenDivision = 1
            }
            // 홈 화면에서 넘어 온 경우
            "Home" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "챙겨야 할 것")
                screenDivision = 2
            }
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭 이벤트
        binding.toolbarStuffAdd.ivSubToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // 소지품 목록 조회 API 연결
        getStuffList(inventoryId)

        // 소지품 클릭 이벤트
        stuffAddListAdapter.onItemClickListener = { position ->
            stuffList[position].isSelected = !stuffList[position].isSelected
            stuffAddListAdapter.notifyItemChanged(position)

            when (screenDivision) {
                // 인벤토리 추가 화면인 경우
                0 -> {
                    // 선택한 소지품이 1개라도 있다면
                    var isClick = false
                    for (i in 0 until stuffList.size) {
                        if (stuffList[i].isSelected) {
                            isClick = true
                            break
                        }
                    }
                    if (isClick) {
                        if (!isEnable) {
                            isEnable = true
                            ButtonUtils().setAllColorAnimation(binding.btnStuffAddAdd)
                        }
                    }
                    else {
                        isEnable = false
                        binding.btnStuffAddAdd.setTextAppearance(R.style.WideButtonDisableStyle)
                        binding.btnStuffAddAdd.setBackgroundColor(requireContext().getColor(R.color.Gray_100))
                    }
                }
                // 인벤토리 수정 화면인 경우
                1 -> {
                    val tempList1 = ArrayList<Boolean>()
                    val tempList2 = ArrayList<Boolean>()
                    for (i in 0 until previousStuffList.size) {
                        tempList1.add(previousStuffList[i].isSelected)
                        tempList2.add(stuffList[i].isSelected)
                    }
                    // 값이 같은지 확인
                    val isSame = compareLists(tempList1, tempList2)
                    if (isSame) {
                        isEnable = false
                        binding.btnStuffAddAdd.setTextAppearance(R.style.WideButtonDisableStyle)
                        binding.btnStuffAddAdd.setBackgroundColor(requireContext().getColor(R.color.Gray_100))
                    }
                    // 다르다면
                    else {
                        if (!isEnable) {
                            isEnable = true
                            ButtonUtils().setAllColorAnimation(binding.btnStuffAddAdd)
                        }
                    }
                }
            }
        }

        // 소지품 추가 완료 버튼 클릭 이벤트
        binding.btnStuffAddAdd.setOnClickListener {
            if (isEnable) {
                val tempStuffNameList = ArrayList<String>()
                for (i in 0 until stuffList.size) {
                    if (stuffList[i].isSelected) {
                        tempStuffNameList.add(stuffList[i].stuffName)
                    }
                }
                // 인벤토리 추가
                addStuffList(inventoryId, StuffAddInventoryModel(tempStuffNameList))
            }
        }
    }

    // 두 개의 리스트의 값이 같은지 확인하는 함수
    private fun compareLists(previousList: ArrayList<Boolean>?, newList: ArrayList<Boolean>): Boolean {
        // 두 리스트의 크기가 다르다면
        if (previousList!!.size != newList.size) {
            return false
        }

        val result = previousList.zip(newList)

        // 두 리스트의 값이 다르다면
        for ((item1, item2) in result) {
            if (item1 != item2) {
                return false
            }
        }

        // 위의 2가지 조건을 모두 통과했다면 값 같음
        return true
    }

    // 소지품 목록 조회 API 연결
    private fun getStuffList(inventoryId: Long) {
        val stuffService = StuffService()
        stuffService.setStuffView(this)
        stuffService.getStuffList(StuffModel(null, inventoryId))
    }

    // 소지품 목록 조회 성공
    override fun onGetStuffListSuccess(status: Int, message: String, data: JsonArray?) {
        when (status) {
            200 -> {
                val jsonArray = JSONArray(data.toString())

                // 데이터 저장
                for (i in 0 until jsonArray.length()) {
                    val subJsonObject = jsonArray.getJSONObject(i)
                    val stuffImgUrl = subJsonObject.getString("stuffImgUrl")
                    val stuffName = subJsonObject.getString("stuffName")
                    val isSelected = subJsonObject.getBoolean("isSelected")

                    stuffList.add(StuffAddItem(stuffImgUrl, stuffName, isSelected))
                    previousStuffList.add(StuffAddItem(stuffImgUrl, stuffName, isSelected))
                }

                binding.rvStuffAddStuff.adapter = stuffAddListAdapter // 어댑터 연결
            }
        }
    }

    // 소지품 목록 조회 실패
    override fun onGetStuffListFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }

    // 인벤토리 내 소지품 추가
    private fun addStuffList(inventoryIdx: Long, stuffName: StuffAddInventoryModel) {
        val stuffService = StuffService()
        stuffService.setCommonView(this)
        stuffService.addStuffList(inventoryIdx, stuffName)
    }

    // 인벤토리 내 소지품 추가 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 인벤토리 수정 완료 팝업 띄우기
                val modifyInventoryDialog = InventoryModifyDialog(requireContext())
                modifyInventoryDialog.showInventoryModifyDialog()
                modifyInventoryDialog.setOnClickedListener(object : InventoryModifyDialog.InventoryModifyBtnClickListener{
                    override fun onClicked() {
                        // 확인 버튼 클릭 시
                        val actionToInventory: NavDirections = StuffAddFragmentDirections.actionStuffAddFrmToInventoryFrm(
                            "Stuff",
                            null
                        )
                        findNavController().navigate(actionToInventory)
                    }
                })
            }
        }
    }

    // 인벤토리 내 소지품 추가 실패
    override fun onCommonFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}