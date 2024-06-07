package com.awkrid.todo.domain.oauth2.service

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import com.awkrid.todo.domain.oauth2.client.oauth2.OAuth2ClientService
import com.awkrid.todo.domain.user.service.UserService
import com.awkrid.todo.infra.swagger.security.jwt.jwtHelper
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
    private val oAuth2ClientService: OAuth2ClientService,
    private val userService: UserService,
    private val jwtHelper: jwtHelper
) {

    fun login(provider: OAuth2Provider, code: String): String {
        return oAuth2ClientService.login(provider, code)
            .let { userService.registerIfAbsent(it) }
            .let { jwtHelper.generateAccessToken(it.id.toString(), it.name, it.role) }
    }
}