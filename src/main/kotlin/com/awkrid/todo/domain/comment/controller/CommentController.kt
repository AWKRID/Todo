package com.awkrid.todo.domain.comment.controller

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos/{todoId}/comments")
@RestController
class CommentController {

    @GetMapping
    fun getCommentList(@PathVariable("todoId") todoId: Long): ResponseEntity<List<CommentResponse>> {
        TODO()
    }

    @GetMapping("/{commentId}")
    fun getComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<CommentResponse> {
        TODO()
    }

    @PostMapping
    fun addComment(
        @PathVariable("todoId") todoId: Long,
        @RequestBody addCommentRequest: AddCommentRequest
    ): ResponseEntity<CommentResponse> {
        TODO()
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest,
    ): ResponseEntity<CommentResponse> {
        TODO()
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<Unit> {
        TODO()
    }

}