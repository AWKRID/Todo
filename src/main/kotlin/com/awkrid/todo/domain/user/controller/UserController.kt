package com.awkrid.todo.domain.user.controller

import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.dto.UserResponse
import com.awkrid.todo.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequest))
    }
}