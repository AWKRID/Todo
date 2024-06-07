package com.awkrid.todo.domain.oauth2.client.oauth2

import com.awkrid.todo.domain.oauth2.OAuth2Provider

open class OAuth2LoginUserInfo(
    val provider: OAuth2Provider,
    val id: String,
    val name: String
)
