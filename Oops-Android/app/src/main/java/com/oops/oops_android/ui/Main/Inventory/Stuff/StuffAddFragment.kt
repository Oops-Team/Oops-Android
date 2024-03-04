package com.oops.oops_android.ui.Main.Inventory.Stuff

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.oops.oops_android.R
import com.oops.oops_android.databinding.FragmentStuffAddBinding
import com.oops.oops_android.ui.Base.BaseFragment

/* 챙겨야 할 것 추가 & 물품 추가 화면 */
class StuffAddFragment: BaseFragment<FragmentStuffAddBinding>(FragmentStuffAddBinding::inflate) {
    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        val args: StuffAddFragmentArgs by navArgs()
        // 툴 바 제목 설정
        when (args.stuffDivision) {
            // 인벤토리 화면에서 넘어 온 경우
            "Inventory" -> {
                setToolbarTitle(binding.toolbarStuffAdd.tvSubToolbarTitle, "물품 추가")
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
    }
}