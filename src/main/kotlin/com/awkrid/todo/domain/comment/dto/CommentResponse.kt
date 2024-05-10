package com.awkrid.todo.domain.comment.dto

import com.awkrid.todo.domain.user.dto.UserResponse

data class CommentResponse(
    val id: Long,
    val text: String,
    val user: UserResponse
)
