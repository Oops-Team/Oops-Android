package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import com.oops.oops_android.R
import com.oops.oops_android.databinding.DialogEditStuffBinding
import com.oops.oops_android.ui.Main.Home.HomeInventoryItem
import com.oops.oops_android.ui.Main.MainActivity
import java.lang.Exception

/* 홈 화면 -> 소지품 수정의 인벤토리 변경 화면 */
class EditStuffDialog(
    private val context: Context,
    private val mainActivity: MainActivity,
    private val inventoryList: ArrayList<HomeInventoryItem>
) {
    private var mBinding: DialogEditStuffBinding? = null
    private val binding get() = mBinding!!

    // dialog 띄우기
    fun showEditStuffDialog() {
        mBinding = DialogEditStuffBinding.inflate(mainActivity.layoutInflater)

        // dialog 설정
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
        dialog.window?.setDimAmount(0.7f) // 배경 투명도 30%
        dialog.setContentView(binding.root) // xml 적용
        dialog.setCancelable(true) // dialog의 바깥 화면 클릭 시 dialog 닫히도록 함
        dialog.show()

        // 현재 선택 중인 인벤토리 정보
        try {
            binding.tvDialogEditStuffInventory1.text = inventoryList[0].inventoryName
            setInventoryItem(binding.ivDialogEditStuffInventory1)

            binding.tvDialogEditStuffInventory2.text = inventoryList[1].inventoryName
            setInventoryItem(binding.ivDialogEditStuffInventory2)

            binding.tvDialogEditStuffInventory3.text = inventoryList[2].inventoryName
            setInventoryItem(binding.ivDialogEditStuffInventory3)

        } catch (e: Exception) {
            Log.d("EditStuffDialog", "인벤토리 파싱 오류! 오류나도 괜찮음")
        }

        // 인벤토리 1 버튼을 누른 경우
        binding.lLayoutDialogEditStuffInventory1.setOnClickListener {
            dialog.dismiss() // dialog 숨기기
            onClickListener.onClicked(inventoryList[0])
        }

        // 인벤토리 2 버튼을 누른 경우
        binding.lLayoutDialogEditStuffInventory2.setOnClickListener {
            dialog.dismiss() // dialog 숨기기
            onClickListener.onClicked(inventoryList[1])
        }

        // 인벤토리 3 버튼을 누른 경우
        binding.lLayoutDialogEditStuffInventory3.setOnClickListener {
            dialog.dismiss() // dialog 숨기기
            onClickListener.onClicked(inventoryList[2])
        }
    }

    // 인벤토리 아이템 넣는 로직
    private fun setInventoryItem(imageView: ImageView) {
        for (i in 0 until inventoryList.size) {
            when (inventoryList[i].inventoryIconIdx) {
                1 -> imageView.setImageResource(R.drawable.ic_time_inventory_20)
                2 -> imageView.setImageResource(R.drawable.ic_run_inventory_20)
                3 -> imageView.setImageResource(R.drawable.ic_wallet_inventory_25)
                4 -> imageView.setImageResource(R.drawable.ic_computer_inventory_25)
                5 -> imageView.setImageResource(R.drawable.ic_wheel_inventory_21)
            }
            break
        }
    }

    interface ButtonClickListener {
        fun onClicked(homeInventoryItem: HomeInventoryItem)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }
}