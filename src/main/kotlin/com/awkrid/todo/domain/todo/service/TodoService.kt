package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication

interface TodoService {
    fun getAllTodoList(name: String?, pageable: Pageable): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest, authentication: Authentication): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest, authentication: Authentication): TodoResponse

    fun deleteTodo(todoId: Long, authentication: Authentication)

}