package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.comment.model.toResponse
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.todo.model.toResponse
import com.awkrid.todo.domain.todo.repository.TodoRepository
import com.awkrid.todo.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) : TodoService {
    override fun getAllTodoList(): List<TodoResponse> {
        return todoRepository.findAll().map{it.toResponse()}
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        val user = userRepository.findByIdOrNull(request.userId) ?: throw ModelNotFoundException("User",request.userId)
        val todo = Todo(
            title = request.title,
            description = request.description,
            user = user
        )
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        val (title, description) = request
        todo.title = title
        todo.description = description
        if(request.isDone) todo.done()
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        todoRepository.delete(todo)
    }

    override fun getCommentList(todoId: Long): List<CommentResponse> {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        return todo.comments.map{it.toResponse()}
    }

    override fun getCommentById(todoId: Long, commentId: Long): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment",commentId)
        return comment.toResponse()
    }

    @Transactional
    override fun addComment(todoId: Long, request: AddCommentRequest): CommentResponse {
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
    override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findByTodoIdAndId(todoId,commentId) ?: throw ModelNotFoundException("Comment",commentId)
        comment.text = request.text
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    override fun deleteComment(todoId: Long, commentId: Long) {
        val comment = commentRepository.findByTodoIdAndId(todoId,commentId) ?: throw ModelNotFoundException("Comment",commentId)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo",todoId)
        todo.removeComment(comment)
        commentRepository.delete(comment)
    }
}