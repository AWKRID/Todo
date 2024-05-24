package com.awkrid.todo.domain.todo.model

import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.comment.model.toResponse
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.toResponse
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

    @OneToMany(mappedBy = "todo")
    val comments: MutableList<Comment> = mutableListOf(),

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun addComment(comment: Comment) {
        comments.add(comment)
    }

    fun removeComment(comment: Comment) {
        comments.remove(comment)
    }

    fun updateTodo(request: UpdateTodoRequest){
        if(request.isDone) isDone = true
        title = request.title
        description = request.description
    }
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(
        id = id!!,
        title = title,
        description = description,
        date = date,
        comments = comments.map{it.toResponse()},
        user = user.toResponse(),
        isDone = isDone
    )
}