package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl : TodoService {
    override fun getAllTodoList(): List<TodoResponse> {
        TODO("Not yet implemented")
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun updateTodo(request: UpdateTodoRequest): TodoResponse {
        TODO("Not yet implemented")
    }

    override fun deleteTodo(todoId: Long) {
        TODO("Not yet implemented")
    }

    override fun getCommentList(todoId: Long): List<CommentResponse> {
        TODO("Not yet implemented")
    }

    override fun getCommentById(todoId: Long, commentId: Long): CommentResponse {
        TODO("Not yet implemented")
    }

    override fun addComment(todoId: Long, request: AddCommentRequest): CommentResponse {
        TODO("Not yet implemented")
    }

    override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        TODO("Not yet implemented")
    }

    override fun deleteComment(todoId: Long, commentId: Long) {
        TODO("Not yet implemented")
    }
}