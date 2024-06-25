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
import com.awkrid.todo.infra.swagger.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class CommentService(
    val todoRepository: TodoRepository,
    val commentRepository: CommentRepository,
    val userRepository: UserRepository
) {

    fun getCommentList(todoId: Long): List<CommentResponse> {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.comments.map { CommentResponse.from(it) }
    }

    fun getCommentById(todoId: Long, commentId: Long): CommentResponse {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return CommentResponse.from(comment)
    }

    @Transactional
    fun addComment(todoId: Long, request: AddCommentRequest, authentication: Authentication): CommentResponse {
        val userPrincipal = authentication.principal as UserPrincipal
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val user =
            userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User", userPrincipal.id)
        val comment = Comment(
            user = user,
            text = request.text,
            todo = todo,
        ).let { commentRepository.save(it) }

        todo.addComment(comment)
        return CommentResponse.from(comment)
    }

    @Transactional
    fun updateComment(
        todoId: Long,
        commentId: Long,
        request: UpdateCommentRequest,
        authentication: Authentication
    ): CommentResponse {
        val userPrincipal = authentication.principal as UserPrincipal
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        if (comment.user.id != userPrincipal.id) {
            throw InvalidCredentialException()
        }
        comment.text = request.text
        return commentRepository.save(comment)
            .let { CommentResponse.from(it) }
    }

    @Transactional
    fun deleteComment(todoId: Long, commentId: Long, authentication: Authentication) {
        val userPrincipal = authentication.principal as UserPrincipal
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)
        if (comment.user.id != userPrincipal.id) {
            throw InvalidCredentialException()
        }
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todo.removeComment(comment)
        commentRepository.delete(comment)
    }
}