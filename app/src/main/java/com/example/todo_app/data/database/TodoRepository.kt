package com.example.todo_app.data.database

import com.example.todo_app.data.database.TodoEntity
import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: LiveData<List<TodoEntity>> = todoDao.getAllTodos()

    fun getFilteredTodos(isDone: Boolean): LiveData<List<TodoEntity>> {
        return todoDao.getFilteredTodos(isDone)
    }
    fun getTodosByDateRange(start: Long, end: Long): LiveData<List<TodoEntity>> {
        return todoDao.getTodosByDateRange(start, end)
    }

    suspend fun insert(todo: TodoEntity) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: TodoEntity) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: TodoEntity) {
        todoDao.delete(todo)
    }
}