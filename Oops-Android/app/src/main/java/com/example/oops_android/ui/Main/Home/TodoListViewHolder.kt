package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.R
import com.example.oops_android.databinding.ItemHomeTodoBinding

class TodoListViewHolder(val context: Context, val binding: ItemHomeTodoBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TodoItem) {
        // 데이터 적용
        val todoName = binding.tvHomeTodoName
        val isComplete = binding.iBtnHomeTodoComplete

        todoName.text = item.todoName

        Log.d("데이터", "true")

        if (item.isComplete)
            isComplete.setImageResource(R.drawable.ic_todo_selected_rbtn_27)
        else
            isComplete.setImageResource(R.drawable.ic_todo_default_rbtn_27)
    }
}
