package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.todo.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TodoService {
    fun getAllTodoList(pageable: Pageable, filter: TodoFilter): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponseWithComments

    fun createTodo(request: CreateTodoRequest, userId: Long): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse

    fun deleteTodo(todoId: Long, userId: Long)

}