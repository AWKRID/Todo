package com.awkrid.todo.domain.todo.dto

import com.awkrid.todo.domain.user.dto.UserResponse
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val date: LocalDateTime,
    val user: UserResponse,
    val isDone: Boolean
)