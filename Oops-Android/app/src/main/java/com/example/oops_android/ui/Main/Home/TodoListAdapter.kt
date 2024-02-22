package com.example.oops_android.ui.Main.Home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oops_android.databinding.ItemHomeTodoBinding

class TodoListAdapter(val context: Context): RecyclerView.Adapter<TodoListViewHolder>() {

    private var todoList = ArrayList<TodoItem>() // 일정 목록
    var onItemClickListener: ((Int, ImageView, TextView, EditText) -> Unit)? = null // 일정의 ... 버튼 클릭

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
            onItemClickListener?.invoke(
                position,
                holder.binding.ivHomeTodoEdit,
                holder.binding.tvHomeTodoName,
                holder.binding.edtHomeTodoName
            )
        }
    }

    // 일정 리스트 반환
    fun getAllTodoList(): ArrayList<TodoItem> = todoList

    // 일정 추가
    fun addTodoList(todoItem: TodoItem) {
        todoList.add(todoItem)
        notifyItemChanged(todoList.size)
    }

    // 일정 정보 내보내기
    fun getTodoList(itemPos: Int): TodoItem {
        return todoList[itemPos]
    }

    // 일정 이름 수정하기
    fun modifyTodoList(itemPos: Int, name: String) {
        // 일정 이름이 변경되었다면
        if (todoList[itemPos].todoName != name) {
            todoList[itemPos].todoName = name
            notifyItemChanged(itemPos)
        }
    }

    // 일정 삭제하기
    @SuppressLint("NotifyDataSetChanged")
    fun deleteTodoList(todoItem: TodoItem?) {
        val index = todoList.indexOf(todoItem)
        todoList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, todoList.size - index)

        //notifyItemRangeRemoved(itemPos, todoList.size - itemPos)
//        for (i in 0 until todoList.size) {
//            if (todoList[i] == todoItem) {
//                todoList.removeAt(i)
//                notifyItemRemoved(i)
//                break
//            }
//        }
    }
}