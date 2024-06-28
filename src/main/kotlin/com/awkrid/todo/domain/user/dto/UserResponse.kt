package com.awkrid.todo.domain.user.dto

import com.awkrid.todo.domain.user.model.User

data class UserResponse(
    val id: Long,
    val name: String,
    val role: String,
    val profileImageUrl: String? = null,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                role = user.role.name,
                profileImageUrl = user.profileImageUrl
            )
        }
    }
}