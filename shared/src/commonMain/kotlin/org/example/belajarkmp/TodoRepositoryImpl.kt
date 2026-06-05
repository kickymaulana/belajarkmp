package org.example.belajarkmp

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.internal.decodeStringToJsonTree

class TodoRepositoryImpl: TodoRepository {
    private val settings = Settings()
    private val KEY_TODO_LIST = "key_todo_list"
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())

    init {

    }

    override fun getTodos(): Flow<List<TodoItem>> = _todoList.asStateFlow()

    override fun saveTodos(todos: List<TodoItem>) {
        try {
            val jsonString = Json.encodeToString(_todoList.value)
            settings.putString(KEY_TODO_LIST, jsonString)
            _todoList.value = todos
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun loadSavedTodos(){
        val jsonString = settings.getString(KEY_TODO_LIST, "")
        if (jsonString.isBlank()) return
        try {
            val savedList = Json.decodeFromString<List<TodoItem>>(jsonString)
            _todoList.value = savedList
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}