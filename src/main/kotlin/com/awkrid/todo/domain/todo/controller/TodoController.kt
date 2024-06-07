package com.awkrid.todo.domain.todo.controller

import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.todo.service.TodoService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos")
@RestController
class TodoController(
    private val todoService: TodoService,
) {

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.getTodoById(todoId))
    }

    @GetMapping
    fun getTodoList(
        @PageableDefault(size = 10, sort = ["date"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(value = "name", required = false) name: String?,
    ): ResponseEntity<Page<TodoResponse>> {
        pageable.sort
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.getAllTodoList(name, pageable))
    }

    @PostMapping
    fun createTodo(
        @Valid @RequestBody createTodoRequest: CreateTodoRequest,
        authentication: Authentication
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(todoService.createTodo(createTodoRequest, authentication))
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @Valid @RequestBody updateTodoRequest: UpdateTodoRequest,
        authentication: Authentication,
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.updateTodo(todoId, updateTodoRequest, authentication))
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @PathVariable todoId: Long,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        todoService.deleteTodo(todoId, authentication)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

}