package com.awkrid.todo.domain.comment.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.repository.TodoRepository
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentService(
    val todoRepository: TodoRepository,
    val commentRepository: CommentRepository,
    val userRepository: UserRepository
) {

    fun getCommentList(todoId: Long): List<CommentResponse> {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return commentRepository.findAllByTodoId(todo.id!!)
            .map { CommentResponse.from(it) }

    }

    fun getCommentById(todoId: Long, commentId: Long): CommentResponse {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return CommentResponse.from(comment)
    }

    @Transactional
    fun addComment(todoId: Long, request: AddCommentRequest, userId: Long): CommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val user =
            userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        val comment = Comment(
            user = user,
            text = request.text,
            todo = todo,
        ).let { commentRepository.save(it) }

        return CommentResponse.from(comment)
    }

    @Transactional
    fun updateComment(
        todoId: Long,
        commentId: Long,
        request: UpdateCommentRequest,
        userId: Long,
    ): CommentResponse {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        if (comment.user.id != userId) {
            throw InvalidCredentialException()
        }
        comment.text = request.text
        return commentRepository.save(comment)
            .let { CommentResponse.from(it) }
    }

    @Transactional
    fun deleteComment(todoId: Long, commentId: Long, userId: Long) {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        if (comment.user.id != userId) throw InvalidCredentialException()
        commentRepository.delete(comment)
    }
}