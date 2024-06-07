package com.awkrid.todo.domain.oauth2.client.oauth2

import com.awkrid.todo.domain.oauth2.OAuth2Provider

interface OAuth2Client {
    fun generateLoginPageUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun getUserInfo(accessToken: String): OAuth2LoginUserInfo
    fun supports(provider: OAuth2Provider): Boolean
}