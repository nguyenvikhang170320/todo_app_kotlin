package com.example.todo_app.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.database.TodoDatabase
import com.example.todo_app.data.database.TodoEntity
import com.example.todo_app.data.database.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository
        private val _filterStatus = MutableLiveData<FilterStatus>() // MutableLiveData to hold the current filter state
    val allTodos = MediatorLiveData<List<TodoEntity>>()
    private var currentSource: LiveData<List<TodoEntity>>? = null// The LiveData that MainActivity observes

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(todoDao)

        _filterStatus.value = FilterStatus.ALL

        _filterStatus.observeForever { status ->
            currentSource?.let { allTodos.removeSource(it) }

            val newSource = when (status) {
                FilterStatus.ALL -> repository.allTodos
                FilterStatus.COMPLETED -> repository.getFilteredTodos(true)
                FilterStatus.PENDING -> repository.getFilteredTodos(false)
            }

            currentSource = newSource
            allTodos.addSource(newSource) { list ->
                allTodos.value = list
            }
        }
    }

    fun insert(todo: TodoEntity) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: TodoEntity) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: TodoEntity) = viewModelScope.launch {
        repository.delete(todo)
    }

    // Function to change the filter status, called from MainActivity
    fun setFilterStatus(status: FilterStatus) {
        _filterStatus.value = status
    }
    fun filterByDateRange(start: Long, end: Long) {
        currentSource?.let { allTodos.removeSource(it) }
        val newSource = repository.getTodosByDateRange(start, end)
        currentSource = newSource
        allTodos.addSource(newSource) { list ->
            allTodos.value = list
        }
    }
}
