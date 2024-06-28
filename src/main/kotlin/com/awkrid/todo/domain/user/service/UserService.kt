package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.oauth2.client.oauth2.OAuth2LoginUserInfo
import com.awkrid.todo.domain.user.dto.LoginRequest
import com.awkrid.todo.domain.user.dto.LoginResponse
import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.dto.UserResponse
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun signUp(request: SignUpRequest): UserResponse
    fun login(request: LoginRequest): LoginResponse
    fun registerIfAbsent(userInfo: OAuth2LoginUserInfo): UserResponse
    fun uploadProfileImage(file: MultipartFile, userId: Long): UserResponse
}