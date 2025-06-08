package com.example.todo_app.data.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    var isDone: Boolean,
    val createdAt: Long = System.currentTimeMillis() // thêm dòng này
)
