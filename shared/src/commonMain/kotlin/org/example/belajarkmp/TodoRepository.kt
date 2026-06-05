package org.example.belajarkmp

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<TodoItem>>
    fun saveTodos(todos: List<TodoItem>)
}