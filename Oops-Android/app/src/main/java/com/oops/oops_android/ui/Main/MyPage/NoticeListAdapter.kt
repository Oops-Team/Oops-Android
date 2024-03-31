package com.oops.oops_android.ui.Main.MyPage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemNoticeBoxRvBinding

/* 공지사항 목록 어댑터 */
class NoticeListAdapter(val context: Context): RecyclerView.Adapter<NoticeListViewHolder>() {

    private var noticeList = ArrayList<NoticeItem>() // 공지사항 리스트
    var onItemClickListener: ((Int) -> Unit)? = null // 공지사항 목록 클릭 이벤트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeListViewHolder {
        val binding: ItemNoticeBoxRvBinding = ItemNoticeBoxRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoticeListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = noticeList.size

    override fun onBindViewHolder(holder: NoticeListViewHolder, position: Int) {
        holder.bind(noticeList[position])

        // 공지사항 클릭
        holder.binding.lLayoutItemNoticeBoxRv.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    // 공지사항 추가
    fun addNoticeList(noticeItem: NoticeItem) {
        noticeList.add(noticeItem)
        notifyItemChanged(noticeList.size)
    }

    // 공지사항 정보 가져오기
    fun getNotice(position: Int): NoticeItem = noticeList[position]
}