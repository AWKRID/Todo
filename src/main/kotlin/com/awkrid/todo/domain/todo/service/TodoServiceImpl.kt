package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.todo.repository.TodoRepository
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.swagger.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
) : TodoService {
    override fun getAllTodoList(name: String?, pageable: Pageable): Page<TodoResponse> {
        val pageTodo: Page<Todo> = if (name.isNullOrBlank()) todoRepository.findAll(pageable)
        else todoRepository.findByName(name, pageable)
        return pageTodo.map { TodoResponse.from(it) }

    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return TodoResponse.from(todo)
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest, authentication: Authentication): TodoResponse {
        val userPrincipal = authentication.principal as UserPrincipal
        val user =
            userRepository.findByIdOrNull(userPrincipal.id) ?: throw ModelNotFoundException("User", userPrincipal.id)
        val todo = Todo(
            title = request.title,
            description = request.description,
            user = user
        )
        return todoRepository.save(todo).let {
            TodoResponse.from(it)
        }
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest, authentication: Authentication): TodoResponse {
        val userPrincipal = authentication.principal as UserPrincipal
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        if (todo.user.id != userPrincipal.id) {
            throw InvalidCredentialException()
        }
        todo.updateTodo(request)
        return todoRepository.save(todo).let {
            TodoResponse.from(it)
        }
    }

    @Transactional
    override fun deleteTodo(todoId: Long, authentication: Authentication) {
        val userPrincipal = authentication.principal as UserPrincipal
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        if (todo.user.id != userPrincipal.id) {
            throw InvalidCredentialException()
        }
        todoRepository.delete(todo)
    }

}