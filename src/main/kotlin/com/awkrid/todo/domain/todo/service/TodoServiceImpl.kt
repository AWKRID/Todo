package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.todo.model.toResponse
import com.awkrid.todo.domain.todo.repository.TodoRepository
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
) : TodoService {
    override fun getAllTodoList(name: String?, pageable: Pageable): Page<TodoResponse> {
        val pageTodo: Page<Todo> = if (name.isNullOrBlank()) todoRepository.findAll(pageable)
        else todoRepository.findByName(name, pageable)
        return pageTodo.map { it.toResponse() }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        val user = userRepository.findByIdOrNull(request.userId) ?: throw ModelNotFoundException("User", request.userId)
        val todo = Todo(
            title = request.title,
            description = request.description,
            user = user
        )
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todo.updateTodo(request)
        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todoRepository.delete(todo)
    }

}