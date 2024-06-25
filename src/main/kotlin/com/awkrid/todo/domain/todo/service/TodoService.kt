package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TodoService {
    fun getAllTodoList(name: String?, pageable: Pageable): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest, userId: Long): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse

    fun deleteTodo(todoId: Long, userId: Long)

}