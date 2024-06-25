package com.awkrid.todo.domain.todo.dto

import jakarta.validation.constraints.Size

data class CreateTodoRequest(
    @field:Size(max = 200, min = 1, message = "제목은 1~200자 이내로 작성해주세요.")
    val title: String,
    @field:Size(max = 1000, min = 1, message = "내용은 1~1000자 이내로 작성해주세요.")
    val description: String,
    val category: String,
    val tags: List<String>
)
