package com.awkrid.todo.domain.oauth2.client.oauth2

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import jakarta.transaction.NotSupportedException
import org.springframework.stereotype.Service

@Service
class OAuth2ClientService(
    private val clients: List<OAuth2Client>
) {
    fun generateLoginPageUrl(provider: OAuth2Provider): String {
        val client = this.selectClient(provider)
        return client.generateLoginPageUrl()
    }

    fun login(provider: OAuth2Provider, authorizationCode: String): OAuth2LoginUserInfo {
        val client = this.selectClient(provider)
        return client.getAccessToken(authorizationCode)
            .let { client.getUserInfo(it) }
    }

    fun selectClient(provider: OAuth2Provider): OAuth2Client {
        return clients.find { it.supports(provider) } ?: throw NotSupportedException("Not supported OAuth2 provider")
    }

}