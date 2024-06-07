package com.awkrid.todo.domain.user.dto

data class UserResponse(
    val id: Long,
    val name: String,
    val role: String
)