package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.dto.UserResponse
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.toResponse
import com.awkrid.todo.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun signUp(request: SignUpRequest): UserResponse {
        if(userRepository.existsByName(request.name)){
            throw IllegalStateException("Username already taken")
        }
        return userRepository.save(
            User(
                name = request.name,
                // TODO: password 암호화하기
                password = request.passwd,
            )
        ).toResponse()
    }
}