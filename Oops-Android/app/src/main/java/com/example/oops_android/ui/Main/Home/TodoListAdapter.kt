package com.example.oops_android.ui.Main.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemHomeTodoBinding

class TodoListAdapter(val context: Context): RecyclerView.Adapter<TodoListViewHolder>() {

    private var todoList = ArrayList<TodoItem>() // 일정 목록
    var onItemClickListener: ((Int, ImageView) -> Unit)? = null // 일정의 ... 버튼 클릭

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val binding: ItemHomeTodoBinding = ItemHomeTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TodoListViewHolder(context, binding)
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bind(todoList[position])

        // ... 클릭
        holder.binding.ivHomeTodoEdit.setOnClickListener {
            onItemClickListener?.invoke(position, holder.binding.ivHomeTodoEdit)
        }
    }

    // 일정 추가
    fun addTodoList(todoItem: TodoItem) {
        todoList.add(todoItem)
        notifyItemChanged(todoList.size)
    }
}