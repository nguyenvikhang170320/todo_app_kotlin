package com.example.todo_app.data.database

import com.example.todo_app.data.database.TodoEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE isDone = :isDone ORDER BY id DESC")
    fun getFilteredTodos(isDone: Boolean): LiveData<List<TodoEntity>>

    // ✅ Thêm hàm lọc theo ngày
    @Query("SELECT * FROM todo_table WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt ASC")
    fun getTodosByDateRange(start: Long, end: Long): LiveData<List<TodoEntity>>

    @Insert
    suspend fun insert(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)
}
