package com.awkrid.todo.domain.comment.dto

import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.user.dto.UserResponse

data class CommentResponse(
    val id: Long,
    val text: String,
    val user: UserResponse
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                text = comment.text,
                user = UserResponse.from(comment.user)
            )
        }
    }
}
