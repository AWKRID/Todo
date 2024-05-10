package com.awkrid.todo.domain.user.repository

import com.awkrid.todo.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean

}