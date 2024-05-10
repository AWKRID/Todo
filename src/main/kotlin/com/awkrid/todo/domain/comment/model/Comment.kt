package com.awkrid.todo.domain.comment.model

import com.awkrid.todo.domain.comment.dto.CommentResponse
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.toResponse
import jakarta.persistence.*


@Entity
@Table(name = "comment")
class Comment(
    @Column(name = "text")
    var text: String,

    @ManyToOne
    @JoinColumn(name = "todo_id")
    val todo: Todo,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        id = id!!,
        text = text,
        user = user.toResponse()
    )
}