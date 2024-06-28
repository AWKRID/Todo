package com.awkrid.todo.domain.user.model

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import jakarta.persistence.*

@Entity
@Table(name = "app_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "password", nullable = true)
    var password: String? = null,

    @Column(name = "profile_image_url")
    var profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = true)
    var provider: OAuth2Provider? = null,

    @Column(name = "provider_id", nullable = true)
    var providerId: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole
) {

    fun isSocialLoginUser(): Boolean {
        return provider != null
    }
}
