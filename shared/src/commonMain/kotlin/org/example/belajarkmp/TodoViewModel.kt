package org.example.belajarkmp

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

class TodoViewModel : ViewModel(){

    private val settings: Settings = Settings()
    private val KEY_TODO_LIST = "key_todo_list"

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList.asStateFlow()

    init {
        loadSavedTodos()
    }

    private fun loadSavedTodos() {
        val jsonString = settings.getString(KEY_TODO_LIST, "")
        if (jsonString.isNotEmpty()) {
            try {
                val savedList = Json.decodeFromString<List<TodoItem>>(jsonString)
                _todoList.value = savedList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun savedTodosToStorage(){
        try {
            val jsonString = Json.encodeToString(_todoList.value)
            settings.putString(KEY_TODO_LIST, jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addTodo(title: String){
        if (title.isBlank()) return
        val newItem = TodoItem(
            id = Random.nextLong(),
            title = title
        )
        _todoList.update { currentList -> currentList + newItem }
        savedTodosToStorage()
    }

    fun toggleTodo(id: Long){
        _todoList.update { currentList ->
            currentList.map { item ->
                if (item.id == id) item.copy(isCompleted = !item.isCompleted) else item
            }
        }
        savedTodosToStorage()
    }

    fun deleteTodo(id: Long){
        _todoList.update { currentList ->
            currentList.filter { it.id != id }
        }
        savedTodosToStorage()
    }
}