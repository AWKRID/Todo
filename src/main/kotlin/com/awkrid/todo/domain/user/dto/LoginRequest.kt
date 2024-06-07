package com.awkrid.todo.domain.user.dto

data class LoginRequest(
    val name: String,
    val password: String,
    val role: String,
)