package com.awkrid.todo.domain.user.model

import com.awkrid.todo.domain.user.dto.UserResponse
import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User(
    @Column(name = "name")
    var name: String,

    @Column(name = "password")
    var password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}

fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id!!,
        name = name,
    )
}