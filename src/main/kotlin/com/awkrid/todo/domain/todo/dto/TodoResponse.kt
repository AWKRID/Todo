package com.awkrid.todo.domain.todo.dto

import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.user.dto.UserResponse
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val date: LocalDateTime,
    val isDone: Boolean,
    val comments: List<CommentResponse>,
    val user: UserResponse,
)