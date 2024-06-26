package com.awkrid.todo.domain.user.entity

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class UserTest {

    @Test
    fun `isSocialUser - 소셜 로그인 유저에 대해서 true 반환`() {
        // given
        val socialUser = User(
            name = "김민수-소셜",
            password = "test",
            provider = OAuth2Provider.KAKAO,
            providerId = "123456",
            role = UserRole.USER
        )
        //when
        val result = socialUser.isSocialLoginUser()

        //then
        result shouldBe true
    }

    @Test
    fun `isSocialUser - 일반 로그인 유저에 대해서 false 반환`() {
        //given
        val nonSocialUser = User(
            name = "김민수",
            password = "test",
            provider = null,
            providerId = null,
            role = UserRole.USER
        )

        // when
        val result = nonSocialUser.isSocialLoginUser()

        // then
        result shouldBe false

    }
}