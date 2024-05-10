package com.awkrid.todo.domain.todo.controller

import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.EntityResponse

@RequestMapping("/todos")
@RestController
class TodoController {

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): EntityResponse<TodoResponse> {
        TODO()
    }

    @GetMapping()
    fun getTodoList(): ResponseEntity<List<TodoResponse>> {
        TODO()
    }

    @PostMapping
    fun createTodo(@RequestBody createTodoRequest: CreateTodoRequest): EntityResponse<TodoResponse> {
        TODO()
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): EntityResponse<Unit> {
        TODO()
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        TODO()
    }
}