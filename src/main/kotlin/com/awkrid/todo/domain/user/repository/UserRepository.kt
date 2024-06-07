package com.awkrid.todo.domain.user.repository

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import com.awkrid.todo.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): User?
    fun findByProviderAndProviderId(provider: OAuth2Provider, providerId: String): User?
}