package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.user.dto.LoginRequest
import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.security.jwt.JwtHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val jwtHelper = mockk<JwtHelper>()

    private val userService = UserServiceImpl(
        userRepository = userRepository,
        passwordEncoder = passwordEncoder,
        jwtHelper = jwtHelper
    )

    @Test
    fun `signUp - 이미 존재하는 name에 대해서 IllegalStateException 이 발생하는지 확인`() {
        // given
        val req = SignUpRequest(
            name = "김민수",
            password = "password"
        )
        every { userRepository.existsByName(req.name) } returns true

        // when, then
        shouldThrow<IllegalStateException> {
            userService.signUp(req)
        }.let {
            it.message shouldBe "Username already taken"
        }
    }

    @Test
    fun `signUp - 올바른 입력에 대해서 회원가입 진행`() {
        //given
        val req = SignUpRequest(
            name = "김민수",
            password = "password"
        )
        every { userRepository.existsByName(req.name) } returns false
        every { userRepository.save(any()) } returns User(
            id = 1L,
            name = "김민수",
            password = "password",
            role = UserRole.USER,
        )
        every { passwordEncoder.encode(any()) } returns "passwordEncoded"

        //when
        val resp = userService.signUp(req)

        //then
        verify(exactly = 1) { userRepository.save(any()) }
        resp.name shouldBe req.name
        resp.role shouldBe UserRole.USER.name
    }

    @Test
    fun `login - 존재하지 않는 name에 대해서 ModelNotFoundException 이 발생하는지 확인`() {
        //given
        val req = LoginRequest(
            name = "김민수",
            password = "password",
            role = "USER"
        )
        every { userRepository.findByName(req.name) } returns null
        //when, then
        shouldThrow<ModelNotFoundException> {
            userService.login(req)
        }
    }

    @Test
    fun `login - 비밀번호가 올바르지 않는 경우 InvalidCredentialException 이 발생하는지 확인`() {

        //given
        val req = LoginRequest(
            name = "김민수",
            password = "password",
            role = "USER"
        )
        val user = User(
            id = 1L,
            name = "김민수",
            password = "password1",
            role = UserRole.USER,
        )

        every { userRepository.findByName(any()) } returns user
        every { passwordEncoder.matches(any(), any()) } returns false

        //when, then
        shouldThrow<InvalidCredentialException> {
            userService.login(req)
        }
    }

    @Test
    fun `login - UserRole이 올바르지 않을 경우 InvalidCredentialException 이 발생하는지 확인`() {

        //given
        val req = LoginRequest(
            name = "김민수",
            password = "password",
            role = "WRONG USER"
        )
        val user = User(
            id = 1L,
            name = "김민수",
            password = "password1",
            role = UserRole.USER,
        )

        every { userRepository.findByName(any()) } returns user

        //when, then
        shouldThrow<InvalidCredentialException> {
            userService.login(req)
        }
    }
}