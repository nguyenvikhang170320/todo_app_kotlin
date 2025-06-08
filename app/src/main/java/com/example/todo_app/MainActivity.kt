package com.example.todo_app

import android.os.Bundle
import android.view.Menu // Import này cần thiết cho menu
import android.view.MenuItem // Import này cần thiết cho menu item
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.adapter.TodoAdapter
import com.example.todo_app.data.database.TodoEntity
import com.example.todo_app.data.model.FilterStatus // Import này cần thiết cho filter
import com.example.todo_app.data.model.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter

    // Khai báo ViewModel
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoRecyclerView = findViewById(R.id.todoRecyclerView)

        // Khởi tạo adapter với tất cả các callbacks cần thiết
        // Loại bỏ dòng 'todoAdapter = TodoAdapter { updatedTodo -> todoViewModel.update(updatedTodo) }' bị thừa
        todoAdapter = TodoAdapter(
            onUpdateTodo = { updatedTodo -> // Dành cho thay đổi trạng thái checkbox
                todoViewModel.update(updatedTodo)
            },
            onDeleteTodo = { todoToDelete -> // Dành cho hành động xóa
                todoViewModel.delete(todoToDelete)
            },
            onEditTodo = { todoToEdit -> // Dành cho hành động chỉnh sửa
                showEditTodoDialog(todoToEdit) // Gọi hàm showEditTodoDialog
            }
        )

        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = todoAdapter

        // Quan sát dữ liệu LiveData từ Room
        todoViewModel.allTodos.observe(this, Observer { todos ->
            todoAdapter.setTodos(todos)
        })

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showAddTodoDialog()
        }
    }

    //region Menu và Filtering
    // Thêm hàm này để inflate menu vào ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Sửa ở đây: Sử dụng R.menu.main_menu thay vì R.menu.todo_item_menu
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Thêm hàm này để xử lý sự kiện click vào các item trong menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter_all -> {
                todoViewModel.setFilterStatus(FilterStatus.ALL)
                true
            }
            R.id.action_filter_completed -> {
                todoViewModel.setFilterStatus(FilterStatus.COMPLETED)
                true
            }
            R.id.action_filter_pending -> {
                todoViewModel.setFilterStatus(FilterStatus.PENDING)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

    private fun showAddTodoDialog() {
        val editText = EditText(this)
        editText.hint = "Nhập tên công việc"

        AlertDialog.Builder(this)
            .setTitle("Thêm công việc")
            .setView(editText)
            .setPositiveButton("Thêm") { _, _ ->
                val title = editText.text.toString().trim()
                if (title.isNotEmpty()) {
                    val newTodo = TodoEntity(0, title, false)
                    todoViewModel.insert(newTodo)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Thêm hàm này để hiển thị dialog chỉnh sửa công việc
    private fun showEditTodoDialog(todo: TodoEntity) {
        val editText = EditText(this)
        editText.hint = "Chỉnh sửa công việc"
        editText.setText(todo.title) // Điền trước tiêu đề hiện tại

        AlertDialog.Builder(this)
            .setTitle("Chỉnh sửa công việc")
            .setView(editText)
            .setPositiveButton("Lưu") { _, _ ->
                val newTitle = editText.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    // Tạo một bản sao của TodoEntity với tiêu đề mới
                    val updatedTodo = todo.copy(title = newTitle)
                    todoViewModel.update(updatedTodo) // Cập nhật vào database
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}