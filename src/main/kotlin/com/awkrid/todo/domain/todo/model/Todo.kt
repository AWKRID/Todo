package com.awkrid.todo.domain.todo.model

import com.awkrid.todo.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "todo")
class Todo(
    @Column(name = "title")
    var title: String,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "date")
    val date: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_done")
    var isDone: Boolean = false,

    @ManyToOne()
    @JoinColumn(name = "user_id")
    val user: User,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
