package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.oauth2.client.oauth2.OAuth2LoginUserInfo
import com.awkrid.todo.domain.user.dto.LoginRequest
import com.awkrid.todo.domain.user.dto.LoginResponse
import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.dto.UserResponse
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.security.jwt.JwtHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: JwtHelper
) : UserService {
    override fun signUp(request: SignUpRequest): UserResponse {
        if (userRepository.existsByName(request.name)) throw IllegalStateException("Username already taken")

        return User(
            name = request.name,
            password = passwordEncoder.encode(request.password),
            role = UserRole.USER
        )
            .let { userRepository.save(it) }
            .let { UserResponse.from(it) }
    }

    override fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByName(request.name) ?: throw ModelNotFoundException("User", request.name)
        if (user.role.name != request.role || !passwordEncoder.matches(request.password, user.password)) {
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

    override fun registerIfAbsent(userInfo: OAuth2LoginUserInfo): UserResponse {
        val user = userRepository.findByProviderAndProviderId(userInfo.provider, userInfo.id) ?: run {
            val user = User(
                name = userInfo.name,
                role = UserRole.USER,
                provider = userInfo.provider,
                providerId = userInfo.id
            )
            userRepository.save(user)
        }
        return UserResponse.from(user)
    }
}