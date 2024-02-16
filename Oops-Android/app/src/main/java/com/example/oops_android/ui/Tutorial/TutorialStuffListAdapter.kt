package com.example.oops_android.ui.Tutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.R
import com.example.oops_android.databinding.ItemTutorialStuffBinding

// 튜토리얼3 화면의 소지품 목록 어댑터
class TutorialStuffListAdapter(val context: Context): RecyclerView.Adapter<TutorialStuffListViewHolder>() {
    // 소지품 목록
    private var stuffList: ArrayList<TutorialStuffItem> = arrayListOf(
        TutorialStuffItem(R.drawable.stuff_computer_img, context.getString(R.string.stuff_sunglasses), false),
        TutorialStuffItem(R.drawable.stuff_diary_img, context.getString(R.string.stuff_diary), false),
        TutorialStuffItem(R.drawable.stuff_handcream_img, context.getString(R.string.stuff_hand_cream), false),
        TutorialStuffItem(R.drawable.stuff_notebook_img, context.getString(R.string.stuff_notebook), false),
        TutorialStuffItem(R.drawable.stuff_artificial_tears_img, context.getString(R.string.stuff_artificial_tears), false),
        TutorialStuffItem(R.drawable.stuff_menstrual_products_img, context.getString(R.string.stuff_menstrual_products), false),
        TutorialStuffItem(R.drawable.stuff_earphones_img, context.getString(R.string.stuff_earphones), false),
        TutorialStuffItem(R.drawable.stuff_mouse_img, context.getString(R.string.stuff_mouse), false),
        TutorialStuffItem(R.drawable.stuff_umbrella_img, context.getString(R.string.stuff_umbrella), false),
        TutorialStuffItem(R.drawable.stuff_usb_img, context.getString(R.string.stuff_usb), false),
        TutorialStuffItem(R.drawable.stuff_charger_img, context.getString(R.string.stuff_charger), false),
        TutorialStuffItem(R.drawable.stuff_wallet_img, context.getString(R.string.stuff_wallet), false),
        TutorialStuffItem(R.drawable.stuff_passport_img, context.getString(R.string.stuff_passport), false),
        TutorialStuffItem(R.drawable.stuff_tissue_img, context.getString(R.string.stuff_tissue), false),
        TutorialStuffItem(R.drawable.stuff_dongle_img, context.getString(R.string.stuff_dongle), false),
        TutorialStuffItem(R.drawable.stuff_reading_desk_img, context.getString(R.string.stuff_reading_desk), false),
        TutorialStuffItem(R.drawable.stuff_alcohol_swap_img, context.getString(R.string.stuff_alcohol_swap), false),
        TutorialStuffItem(R.drawable.stuff_sunglasses_img, context.getString(R.string.stuff_sunglasses), false),
    )
    var onStuffItemClickListener: ((Int) -> Unit)? = null // 소지품 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialStuffListViewHolder {
        val binding: ItemTutorialStuffBinding = ItemTutorialStuffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TutorialStuffListViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: TutorialStuffListViewHolder, position: Int) {
        holder.bind(stuffList[position])

        // 소지품 클릭
        holder.binding.lLayoutItemTutorialTop.setOnClickListener {
            onStuffItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = stuffList.size

    // 소지품 클릭 여부 변경
    fun setClickStuffItem(position: Int) {
        stuffList[position].isSelected = !stuffList[position].isSelected
        notifyItemChanged(position)
    }

    // 선택된 소지품 개수 찾기
    fun getClickedStuffList(): Int {
        var count = 0
        for (i in 0 until stuffList.size) {
            if (stuffList[i].isSelected) {
                ++count
            }
        }
        return count
    }
}