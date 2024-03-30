package com.oops.oops_android.ui.Main.MyPage

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.oops.oops_android.databinding.ItemNoticeBoxRvBinding

/* 공지사항 목록 리스트 뷰 홀더 */
class NoticeListViewHolder(val context: Context, val binding: ItemNoticeBoxRvBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NoticeItem) {
        // 데이터 적용
        val noticeTitle = binding.tvItemNoticeBoxRvTitle
        val content = binding.tvItemNoticeBoxRvContent

        noticeTitle.text = item.noticeTitle
        content.text = item.content
    }
}
