package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.data.domain.Pageable

interface TodoService {
    fun getAllTodoList(name: String?, pageable: Pageable): List<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)

    fun getCommentList(todoId: Long): List<CommentResponse>

    fun getCommentById(todoId: Long, commentId: Long): CommentResponse

    fun addComment(todoId: Long, request: AddCommentRequest): CommentResponse

    fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse

    fun deleteComment(todoId: Long, commentId: Long)
}