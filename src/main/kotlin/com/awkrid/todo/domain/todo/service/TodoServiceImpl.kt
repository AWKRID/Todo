package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.category.repository.CategoryRepository
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.dto.*
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.todo.repository.TodoRepository
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val categoryRepository: CategoryRepository,
) : TodoService {
    override fun getAllTodoList(pageable: Pageable, filter: TodoFilter): Page<TodoResponse> {
        return todoRepository.findByPageableAndFilters(pageable, filter)
            .map { TodoResponse.from(it) }
    }

    override fun getTodoById(todoId: Long): TodoResponseWithComments {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val comments = commentRepository.findAllByTodoId(todoId)
        return TodoResponseWithComments.from(todo, comments)
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest, userId: Long): TodoResponse {
        val user =
            userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        val category = categoryRepository.findByName(request.category) ?: throw ModelNotFoundException(
            "Category",
            request.category
        )
        val todo = Todo(
            title = request.title,
            description = request.description,
            user = user,
            tags = toHashTags(request.tags),
            category = category.name
        )
        return todoRepository.save(todo).let {
            TodoResponse.from(it)
        }
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest, userId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val category = categoryRepository.findByName(request.category) ?: throw ModelNotFoundException(
            "Category",
            request.category
        )
        if (todo.user.id != userId) throw InvalidCredentialException()
        todo.apply {
            this.title = request.title
            this.description = request.description
            this.isDone = request.isDone
            this.tags = "#" + request.tags.joinToString("#") + "#"
            this.category = category.name
        }
        return todoRepository.save(todo)
            .let { TodoResponse.from(it) }
    }

    @Transactional
    override fun deleteTodo(todoId: Long, userId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        if (todo.user.id != userId) {
            throw InvalidCredentialException()
        }
        todoRepository.delete(todo)
    }

    private fun toHashTags(tags: List<String>): String = "#" + tags.joinToString("#") + "#"
}