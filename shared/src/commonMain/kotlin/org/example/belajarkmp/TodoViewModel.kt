package org.example.belajarkmp

import androidx.compose.ui.text.font.FontVariation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
data class TodoItem(
    val id: Long,
    val title: String,
    val isCompleted: Boolean = false
)

class TodoViewModel(private val repository: TodoRepository) : ViewModel(){

    val todoList: StateFlow<List<TodoItem>> = repository.getTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTodo(title: String){
        if (title.isBlank()) return
        viewModelScope.launch {
            val currentList = todoList.value
            val newItem = TodoItem(id = Random.nextLong(), title = title)
            repository.saveTodos(currentList + newItem)
        }
    }

    fun toggleTodo(id: Long){
        viewModelScope.launch {
             val updatedList = todoList.value.map { item ->
                 if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
             }
            repository.saveTodos(updatedList)
        }
    }

    fun deleteTodo(id: Long){
        viewModelScope.launch {
            val updatedList = todoList.value.filter { it.id != id }
            repository.saveTodos(updatedList)
        }
    }
}