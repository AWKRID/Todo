package com.awkrid.todo.domain.comment.repository

import com.awkrid.todo.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByTodoIdAndId(todoId: Long, lectureId: Long): Comment?
    fun findAllByTodoId(todoId: Long): List<Comment>
}