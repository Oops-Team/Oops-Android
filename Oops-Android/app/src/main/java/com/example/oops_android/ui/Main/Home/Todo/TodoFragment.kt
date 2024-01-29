package com.example.oops_android.ui.Main.Home.Todo

import androidx.navigation.findNavController
import com.example.oops_android.databinding.FragmentTodoBinding
import com.example.oops_android.ui.Base.BaseFragment

// 일정 추가 & 수정 화면
class TodoFragment: BaseFragment<FragmentTodoBinding>(FragmentTodoBinding::inflate) {
    override fun initViewCreated() {
        // 바텀 네비게이션 숨기기
        mainActivity?.hideBnv(true)

        // 툴 바 제목 설정
        setToolbarTitle(binding.toolbarTodo.tvSubToolbarTitle, "일정 추가")
    }

    override fun initAfterBinding() {
        // 월간 캘린더
        binding.mcvTodoCalendarview.topbarVisible = false // 년도, 좌우 버튼 숨기기

        // 뒤로 가기 버튼 클릭
        binding.toolbarTodo.ivSubToolbarBack.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }

}