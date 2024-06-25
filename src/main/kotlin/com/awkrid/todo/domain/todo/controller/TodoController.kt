package com.awkrid.todo.domain.todo.controller

import com.awkrid.todo.domain.todo.dto.*
import com.awkrid.todo.domain.todo.service.TodoService
import com.awkrid.todo.infra.swagger.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos")
@RestController
class TodoController(
    private val todoService: TodoService,
) {

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponseWithComments> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.getTodoById(todoId))
    }

    @GetMapping
    fun getTodoList(
        @PageableDefault(size = 10, sort = ["date"]) pageable: Pageable,
        filter: TodoFilter
    ): ResponseEntity<Page<TodoResponse>> {
        pageable.sort
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.getAllTodoList(pageable, filter))
    }

    @PostMapping
    fun createTodo(
        @Valid @RequestBody createTodoRequest: CreateTodoRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(todoService.createTodo(createTodoRequest, userPrincipal.id))
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @Valid @RequestBody updateTodoRequest: UpdateTodoRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(todoService.updateTodo(todoId, updateTodoRequest, userPrincipal.id))
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @PathVariable todoId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        todoService.deleteTodo(todoId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

}