package com.oops.oops_android.ui.Main.Inventory.Stuff

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonArray
import com.oops.oops_android.R
import com.oops.oops_android.data.remote.Stuff.Api.StuffService
import com.oops.oops_android.data.remote.Stuff.Api.StuffView
import com.oops.oops_android.data.remote.Stuff.Model.StuffModel
import com.oops.oops_android.databinding.FragmentStuffAddBinding
import com.oops.oops_android.ui.Base.BaseFragment
import org.json.JSONArray

/* 챙겨야 할 것 추가 & 물품 추가 화면 */
class StuffAddFragment: BaseFragment<FragmentStuffAddBinding>(FragmentStuffAddBinding::inflate), StuffView {

    private var inventoryId: Long = 0L // 인벤토리 id
    private var stuffList = ArrayList<StuffAddItem>() // 각 인벤토리 내의 소지품 리스트
    private var stuffAddListAdapter = StuffAddListAdapter(stuffList) // 소지품 리스트 어댑터

    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        val args: StuffAddFragmentArgs by navArgs()
        // 툴 바 제목 설정
        when (args.stuffDivision) {
            // 인벤토리 화면에서 넘어 온 경우
            "Inventory" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "물품 추가")

                // 데이터 저장하기
                inventoryId = args.inventoryId
            }
            // 홈 화면에서 넘어 온 경우
            "Home" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "챙겨야 할 것")
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
    }

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
                }

                binding.rvStuffAddStuff.adapter = stuffAddListAdapter // 어댑터 연결
            }
        }
    }

    // 소지품 목록 조회 실패
    override fun onGetStuffListFailure(status: Int, message: String) {
        showToast(resources.getString(R.string.toast_server_error))
    }
}