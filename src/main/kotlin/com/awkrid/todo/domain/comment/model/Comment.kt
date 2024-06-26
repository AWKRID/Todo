package com.awkrid.todo.domain.comment.model

import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.user.model.User
import jakarta.persistence.*


@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "text")
    var text: String,

    @ManyToOne
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
) {
}
