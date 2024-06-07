package com.awkrid.todo.domain.comment.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.comment.model.toResponse
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.repository.TodoRepository
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
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        return todo.comments.map{it.toResponse()}
    }

    fun getCommentById(todoId: Long, commentId: Long): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment",commentId)
        return comment.toResponse()
    }

    @Transactional
    fun addComment(todoId: Long, request: AddCommentRequest): CommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        val user = userRepository.findByIdOrNull(request.userId) ?: throw ModelNotFoundException("User",request.userId)
        val comment = Comment(
            user = user,
            text = request.text,
            todo = todo,
        ).let{commentRepository.save(it)}

        todo.addComment(comment)
        return comment.toResponse()
    }

    @Transactional
    fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId,commentId) ?: throw ModelNotFoundException("Comment",commentId)
        comment.text = request.text
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun deleteComment(todoId: Long, commentId: Long) {
        val comment = commentRepository.findByTodoIdAndId(todoId,commentId) ?: throw ModelNotFoundException("Comment",commentId)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        todo.removeComment(comment)
        commentRepository.delete(comment)
    }
}