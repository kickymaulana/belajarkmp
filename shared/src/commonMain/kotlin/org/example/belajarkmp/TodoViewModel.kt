package org.example.belajarkmp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

// Data class untuk representasi item tugas
data class TodoItem(
    val id: Long,
    val title: String,
    val isCompleted: Boolean = false
)

class TodoViewModel : ViewModel() {
    // Menggunakan StateFlow untuk reactive state yang aman di multiplatform
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList.asStateFlow()

    fun addTodo(title: String) {
        if (title.isBlank()) return

        val newItem = TodoItem(
            id = Random.nextLong(), // Menggunakan Random untuk generate ID unik di commonMain
            title = title
        )
        _todoList.update { currentList -> currentList + newItem }
    }

    fun toggleTodo(id: Long) {
        _todoList.update { currentList ->
            currentList.map { item ->
                if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
            }
        }
    }

    fun deleteTodo(id: Long) {
        _todoList.update { currentList ->
            currentList.filter { it.id != id }
        }
    }
}
