package com.oops.oops_android.ui.Main.Inventory.Stuff

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemTutorialStuffBinding
import java.util.Locale

// 소지품 추가 화면의 소지품 목록 어댑터
class StuffAddListAdapter(stuffList: ArrayList<StuffAddItem>) : RecyclerView.Adapter<StuffAddListViewHolder>(), Filterable {

    private var tempStuffList = stuffList // 소지품 리스트
    private var filterList = stuffList // 필터링된 소지품 리스트
    var onItemClickListener: ((Int, String) -> Unit)? = null // 소지품 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffAddListViewHolder {
        val binding: ItemTutorialStuffBinding = ItemTutorialStuffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StuffAddListViewHolder(binding)
    }

    override fun getItemCount(): Int = filterList.size

    override fun onBindViewHolder(holder: StuffAddListViewHolder, position: Int) {
        holder.bind(filterList[position])

        // 소지품 클릭
        holder.binding.lLayoutItemTutorialTop.setOnClickListener {
            onItemClickListener?.invoke(position, holder.binding.tvItemTutorialStuff.text.toString())
        }
    }

    // 소지품 키워드 필터링
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filterList = if (charString.isEmpty()) {
                    tempStuffList
                } else {
                    val filteringList = ArrayList<StuffAddItem>()

                    for (item in tempStuffList) {
                        if (item.stuffName.lowercase(Locale.getDefault()).contains(charString)) {
                            filteringList.add(item)
                        }
                    }

                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList

                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filterList = results.values as ArrayList<StuffAddItem>

                notifyDataSetChanged()
            }
        }
    }
}