package com.awkrid.todo.domain.todo.dto

data class TodoFilter(
    val title: String? = null,
    val tag: String? = null,
    val category: String? = null,
    val isDone: Boolean? = null,
    val daysAgo: Long? = null
)
