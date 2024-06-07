package com.awkrid.todo.domain.oauth2.controller

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import com.awkrid.todo.domain.oauth2.client.oauth2.OAuth2ClientService
import com.awkrid.todo.domain.oauth2.service.OAuth2LoginService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Oauth2LoginController(
    private val oAuth2ClientService: OAuth2ClientService,
    private val oAuth2LoginService: OAuth2LoginService
) {

    @GetMapping("/oauth2/login/{provider}")
    fun redirectLoginPage(@PathVariable provider: OAuth2Provider, response: HttpServletResponse) {
        oAuth2ClientService.generateLoginPageUrl(provider)
            .let { response.sendRedirect(it) }
    }

    @GetMapping("/oauth2/callback/{provider}")
    fun callback(
        @PathVariable provider: OAuth2Provider,
        @RequestParam(name = "code") code: String,
    ): String {
        return oAuth2LoginService.login(provider, code)
    }
}