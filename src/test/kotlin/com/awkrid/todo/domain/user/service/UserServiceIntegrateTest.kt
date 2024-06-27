package com.awkrid.todo.domain.user.service

import com.awkrid.todo.domain.user.dto.SignUpRequest
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.security.jwt.JwtHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserServiceIntegrateTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtHelper: JwtHelper,
) {
    private val userService = UserServiceImpl(
        userRepository = userRepository,
        jwtHelper = jwtHelper,
        passwordEncoder = passwordEncoder,
    )

    @Test
    fun `이미 가입되어 있는 이름인 경우 예외 발생 확인`() {
        // given
        val 기존_회원 = User(
            name = "김민수",
            password = "1234",
            role = UserRole.USER
        )
        userRepository.saveAndFlush(기존_회원)
        val request = SignUpRequest(
            name = "김민수",
            password = "1234"
        )

        // when
        shouldThrow<IllegalStateException> {
            userService.signUp(request)
        }.let {
            it.message shouldBe "Username already taken"
        }

        userRepository.findAll()
            .filter { it.name == "김민수" }
            .let {
                it.size shouldBe 1
                it[0].name shouldBe "김민수"
            }

    }
}