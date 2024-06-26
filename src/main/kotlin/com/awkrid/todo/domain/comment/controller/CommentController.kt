package com.awkrid.todo.domain.comment.controller

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.comment.service.CommentService
import com.awkrid.todo.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/todos/{todoId}/comments")
@RestController
class CommentController(
    private val commentService: CommentService
) {

    @GetMapping
    fun getCommentList(@PathVariable("todoId") todoId: Long): ResponseEntity<List<CommentResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getCommentList(todoId))
    }

    @GetMapping("/{commentId}")
    fun getComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getCommentById(todoId, commentId))
    }

    @PostMapping
    fun addComment(
        @PathVariable("todoId") todoId: Long,
        @RequestBody addCommentRequest: AddCommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(commentService.addComment(todoId, addCommentRequest, userPrincipal.id))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(todoId, commentId, updateCommentRequest, userPrincipal.id))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable("todoId") todoId: Long,
        @PathVariable("commentId") commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        commentService.deleteComment(todoId, commentId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

}