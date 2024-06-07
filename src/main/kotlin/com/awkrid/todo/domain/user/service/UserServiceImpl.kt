package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.user.dto.LoginRequest
import com.awkrid.todo.domain.user.dto.LoginResponse
import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.dto.UserResponse
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.model.toResponse
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.swagger.security.jwt.jwtHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: jwtHelper
) : UserService {
    override fun signUp(request: SignUpRequest): UserResponse {
        if(userRepository.existsByName(request.name)){
            throw IllegalStateException("Username already taken")
        }
        return userRepository.save(
            User(
                name = request.name,
                password = passwordEncoder.encode(request.password),
                role = UserRole.USER
            )
        ).toResponse()
    }

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByName(request.name) ?: throw ModelNotFoundException("User",null)
        if(user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)){
            throw InvalidCredentialException()
        }
        return LoginResponse(
            accessToken = jwtHelper.generateAccessToken(
                subject = user.id.toString(),
                name = user.name,
                role = user.role.name,
            )
        )

    }
}