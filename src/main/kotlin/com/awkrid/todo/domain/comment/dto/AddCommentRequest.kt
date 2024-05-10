package com.awkrid.todo.domain.comment.dto

data class AddCommentRequest(
    val userId : Long,
    val text: String
)
