package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonObject
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Common.CommonView
import com.oops.oops_android.data.remote.Stuff.Api.StuffObjectView
import com.oops.oops_android.data.remote.Stuff.Api.StuffService
import com.oops.oops_android.data.remote.Stuff.Model.InventoryChangeTodoModel
import com.oops.oops_android.data.remote.Stuff.Model.StuffDeleteHomeModel
import com.oops.oops_android.databinding.FragmentStuffBinding
import com.oops.oops_android.ui.Base.BaseFragment
import com.oops.oops_android.ui.Main.Home.HomeInventoryItem
import com.oops.oops_android.ui.Main.Home.StuffItem
import org.json.JSONObject
import java.time.LocalDate

/* 챙겨야 할 것 화면 */
class StuffFragment: BaseFragment<FragmentStuffBinding>(FragmentStuffBinding::inflate), CommonView, StuffObjectView {

    private lateinit var selectDate: LocalDate // 일정 날짜
    private var inventoryList = ArrayList<HomeInventoryItem>() // 전체 인벤토리 리스트
    private var stuffList = ArrayList<StuffItem>() // 소지품 목록
    private lateinit var stuffTodoListAdapter: StuffTodoListAdapter // 소지품 어댑터
    private var nowInventoryId: Long = 0 // 현재 사용 중인 인벤토리 아이디

    private val STUFF_TAG = "StuffFragment" // log용

    @SuppressLint("SetTextI18n")
    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        // 툴바 제목 설정
        binding.toolbarStuff.tvSubToolbarTitle.text = resources.getString(R.string.stuff_title)

        // 데이터 가져오기
        val args: StuffFragmentArgs by navArgs()
        try {
            // 일정 날짜
            selectDate = LocalDate.parse(args.todoDate)

            // 전체 인벤토리 리스트
            inventoryList = args.homeInventoryAllList!!.toCollection(ArrayList())

            // 일정 내 소지품 리스트
            stuffList = args.stuffAllList!!.toCollection(ArrayList())

            // 인벤토리 이름 반영
            for (i in 0 until inventoryList.size) {
                if (inventoryList[i].isInventoryUsed) {
                    nowInventoryId = inventoryList[i].inventoryId // 현재 사용 중인 인벤토리 아이디 저장
                    binding.tvStuffInfo.text = "현재 ${inventoryList[i].inventoryName} 인벤토리를 사용 중이에요"
                }
            }
        } catch (e: Exception) {
            Log.e(STUFF_TAG, "Get Nav Data ${e.stackTraceToString()}")
        }
    }

    override fun initAfterBinding() {
        // 뒤로 가기 버튼 클릭 이벤트
        binding.toolbarStuff.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        stuffTodoListAdapter = StuffTodoListAdapter(stuffList) // 소지품 목록 어댑터
        binding.rvStuffStuff.adapter = stuffTodoListAdapter

        // 소지품 롱 클릭 이벤트
        stuffTodoListAdapter.setOnItemLongClickListener(object : StuffTodoListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                // 소지품 삭제 API 연결
                deleteHomeStuff(stuffList[position].stuffName, position)
            }
        })

        // 인벤토리 변경 버튼 클릭 이벤트
        binding.iBtnStuffShowInventory.setOnClickListener {
            // 현재 선택 중인 인벤토리 아이템을 제외하고 다른 화면에 넘겨주기
            val tempInventoryList = ArrayList<HomeInventoryItem>()
            for (i in 0 until inventoryList.size) {
                if (!inventoryList[i].isInventoryUsed) {
                    tempInventoryList.add(inventoryList[i])
                }
            }
            val dialog = EditStuffDialog(requireContext(), mainActivity!!, tempInventoryList)
            dialog.showEditStuffDialog()

            dialog.setOnClickedListener(object : EditStuffDialog.ButtonClickListener {
                override fun onClicked(homeInventoryItem: HomeInventoryItem) {
                    // 선택한 인벤토리로 변경
                    for (i in 0 until inventoryList.size) {
                        inventoryList[i].isInventoryUsed = false
                    }
                    for (i in 0 until inventoryList.size) {
                        if (inventoryList[i].inventoryId == homeInventoryItem.inventoryId) {
                            inventoryList[i].isInventoryUsed = true
                        }
                    }

                    // 다른 인벤토리로 변경 API 연결
                    changeTodoInventory(InventoryChangeTodoModel(selectDate.toString(), homeInventoryItem.inventoryName))
                }
            })
        }

        // 소지품 추가하기 버튼 클릭 이벤트
        binding.btnStuffAdd.setOnClickListener {
            // 소지품 추가 화면으로 이동하기
            val actionToStuffAdd: NavDirections = StuffFragmentDirections.actionStuffFrmToStuffAddFrm(
                "Home",
                selectDate.toString(),
                -1
            )
            findNavController().navigate(actionToStuffAdd)
        }
    }

    // 챙겨야 할 것 수정(소지품 삭제) API 연결
    private fun deleteHomeStuff(stuffName: String, position: Int) {
        val stuffService = StuffService()
        stuffService.setCommonView(this)
        stuffService.deleteHomeStuff(StuffDeleteHomeModel(selectDate.toString(), stuffName), position)
    }

    // 챙겨야 할 것 수정(소지품 삭제) 성공
    override fun onCommonSuccess(status: Int, message: String, data: Any?) {
        when (status) {
            200 -> {
                // 성공
                val pos = data as Int
                stuffList.removeAt(pos)
                stuffTodoListAdapter.notifyItemRemoved(pos)
                stuffTodoListAdapter.notifyItemRangeRemoved(pos, stuffList.size - pos)
            }
        }
    }

    // 챙겨야 할 것 수정(소지품 삭제) 실패
    override fun onCommonFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }

    // 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경) API 연결
    private fun changeTodoInventory(inventoryChangeTodoModel: InventoryChangeTodoModel) {
        val stuffService = StuffService()
        stuffService.setStuffObjectView(this)
        stuffService.changeTodoInventory(inventoryChangeTodoModel)
    }

    // 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경) 성공
    @SuppressLint("NotifyDataSetChanged")
    override fun onGetInventoryListSuccess(status: Int, message: String, data: JsonObject?) {
        when (status) {
            200 -> {
                val jsonObject = JSONObject(data.toString())

                // inventoryId
                val inventoryId = jsonObject.getLong("inventoryId")
                nowInventoryId = inventoryId

                // stuffList
                stuffList.clear()
                val tempStuffList = jsonObject.getJSONArray("stuffList")
                for (i in 0 until tempStuffList.length()) {
                    val subJsonObject = tempStuffList.getJSONObject(i)
                    val stuffImgUrl = subJsonObject.getString("stuffImgUrl")
                    val stuffName = subJsonObject.getString("stuffName")

                    // 아이템 추가
                    stuffList.add(StuffItem(stuffImgUrl, stuffName))
                }
                stuffTodoListAdapter.notifyDataSetChanged()
            }
        }
    }

    // 홈 화면의 챙겨야 할 것 수정(다른 인벤토리로 선택 및 변경) 실패
    override fun onGetInventoryListFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}