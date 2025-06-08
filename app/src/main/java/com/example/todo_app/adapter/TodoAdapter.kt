package com.example.todo_app.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu // Make sure this is androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.data.database.TodoEntity
import java.util.Date
import java.util.Locale

// import kotlinx.coroutines.Job // This import is no longer needed here as Job is not directly used in the adapter constructor

// Corrected constructor to accept the three callback functions
class TodoAdapter(
    private val onUpdateTodo: (TodoEntity) -> Unit, // Callback for checkbox state change
    private val onDeleteTodo: (TodoEntity) -> Unit, // Callback for delete action
    private val onEditTodo: (TodoEntity) -> Unit    // Callback for edit action
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todos = emptyList<TodoEntity>()

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDate: TextView = itemView.findViewById(R.id.textDate)

        init {
            // Add long click listener for item options (Edit/Delete)
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val todo = todos[position]
                    showItemPopupMenu(itemView, todo)
                }
                true // Consume the long click
            }
        }

        private fun showItemPopupMenu(view: View, todo: TodoEntity) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.todo_item_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit_todo -> {
                        onEditTodo(todo) // Now 'onEditTodo' is resolved
                        true
                    }
                    R.id.action_delete_todo -> {
                        onDeleteTodo(todo) // Now 'onDeleteTodo' is resolved
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]

        // Tránh set lại listener khi gọi lại bind để không gây gọi lại callback không mong muốn
        holder.checkBox.setOnCheckedChangeListener(null)

        holder.textTitle.text = todo.title
        holder.checkBox.isChecked = todo.isDone
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString = sdf.format(Date(todo.createdAt))
        holder.textDate.text = dateString
        // Set gạch ngang nếu todo đã hoàn thành
        if (todo.isDone) {
            holder.textTitle.paintFlags = holder.textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.textTitle.paintFlags = holder.textTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Lắng nghe sự kiện thay đổi trạng thái checkBox
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
            onUpdateTodo(todo) // Now 'onUpdateTodo' is resolved
            // notifyItemChanged(position) is generally not needed here if LiveData observes and updates the whole list
            // If you still need it for specific UI effects, ensure it doesn't conflict with LiveData updates.
        }
    }

    override fun getItemCount(): Int = todos.size

    fun setTodos(todos: List<TodoEntity>) {
        this.todos = todos
        notifyDataSetChanged()
    }
}