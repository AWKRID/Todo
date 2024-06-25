package com.awkrid.todo.domain.todo.dto

import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.user.dto.UserResponse
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val date: LocalDateTime,
    val isDone: Boolean,
    val user: UserResponse,
    val tags: List<String>,
    val category: String

) {
    companion object {
        fun from(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id!!,
                title = todo.title,
                description = todo.description,
                date = todo.date,
                isDone = todo.isDone,
                user = UserResponse.from(todo.user),
                tags = todo.tags.split("#").filter(String::isNotEmpty),
                category = todo.category.name
            )
        }
    }
}