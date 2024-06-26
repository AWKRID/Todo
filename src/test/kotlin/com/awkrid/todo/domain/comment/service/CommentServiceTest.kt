package com.awkrid.todo.domain.comment.service

import com.awkrid.todo.domain.comment.dto.AddCommentRequest
import com.awkrid.todo.domain.comment.dto.UpdateCommentRequest
import com.awkrid.todo.domain.comment.model.Comment
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.todo.repository.TodoRepository
import com.awkrid.todo.domain.user.exception.InvalidCredentialException
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class CommentServiceTest {

    private val todoRepository = mockk<TodoRepository>()
    private val commentRepository = mockk<CommentRepository>()
    private val userRepository = mockk<UserRepository>()
    private val commentService = CommentService(
        todoRepository = todoRepository,
        commentRepository = commentRepository,
        userRepository = userRepository,
    )

    @Test
    fun `getCommentList - Todo 없는 경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        every { todoRepository.findByIdOrNull(todoId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.getCommentList(todoId)
        }
    }

    @Test
    fun `getCommentById - comment가 없는 경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        val commentId = 1L

        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.getCommentById(todoId, commentId)
        }
    }

    @Test
    fun `addComment - todo가 없는 경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        val req = AddCommentRequest("test")
        val userId = 1L

        every { todoRepository.findByIdOrNull(todoId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.addComment(todoId, req, userId)
        }
    }

    @Test
    fun `addComment - user가 없는경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        val req = AddCommentRequest("test")
        val userId = 1L
        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )
        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )
        every { todoRepository.findByIdOrNull(todoId) } returns todo
        every { userRepository.findByIdOrNull(userId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.addComment(todoId, req, userId)
        }
    }

    @Test
    fun `addComment - 성공 케이스`() {
        // given
        val todoId = 1L
        val req = AddCommentRequest("test")
        val userId = 1L
        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )
        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )
        every { todoRepository.findByIdOrNull(todoId) } returns todo
        every { userRepository.findByIdOrNull(userId) } returns user
        every { commentRepository.save(any()) } returns Comment(
            id = 1L,
            user = user,
            text = "test",
            todo = todo
        )

        // when
        val resp = commentService.addComment(todoId, req, userId)

        // then
        verify(exactly = 1) { commentRepository.save(any()) }
        resp.text shouldBe "test"
    }

    @Test
    fun `updateComment - comment가 없는 경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val req = UpdateCommentRequest("test")
        val userId = 1L

        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.updateComment(todoId, commentId, req, userId)
        }
    }

    @Test
    fun `updateComment - userId가 다른 경우 InvalidCredentialException 발생`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val req = UpdateCommentRequest("test")
        val userId = 1L

        val user = User(
            id = 2L,
            name = "김민수",
            role = UserRole.USER
        )

        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )

        val comment = Comment(
            id = 1L,
            user = user,
            text = "test",
            todo = todo
        )
        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns comment

        // when, then
        shouldThrow<InvalidCredentialException> {
            commentService.updateComment(todoId, commentId, req, userId)
        }
    }

    @Test
    fun `updateComment - 성공 케이스`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val req = UpdateCommentRequest("test after")
        val userId = 1L

        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )

        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )

        val comment = Comment(
            id = 1L,
            user = user,
            text = "test",
            todo = todo
        )
        val commentAfter = Comment(
            id = 1L,
            user = user,
            text = "test after",
            todo = todo
        )
        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns comment
        every { commentRepository.save(any()) } returns commentAfter

        // when
        val resp = commentService.updateComment(todoId, commentId, req, userId)

        // then
        verify(exactly = 1) { commentRepository.save(any()) }
        resp.text shouldBe "test after"
    }

    @Test
    fun `deleteComment - comment가 없는 경우 ModelNotFoundException 발생`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val userId = 1L

        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            commentService.deleteComment(todoId, commentId, userId)
        }
    }

    @Test
    fun `deleteComment - userId가 다른 경우 InvalidCredentialException 발생`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val userId = 1L

        val user = User(
            id = 2L,
            name = "김민수",
            role = UserRole.USER
        )

        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )

        val comment = Comment(
            id = 1L,
            user = user,
            text = "test",
            todo = todo
        )
        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns comment

        // when, then
        shouldThrow<InvalidCredentialException> {
            commentService.deleteComment(todoId, commentId, userId)
        }
    }

    @Test
    fun `deleteComment - 성공 케이스`() {
        // given
        val todoId = 1L
        val commentId = 1L
        val userId = 1L

        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )

        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리",
            tags = "#태그#",
            user = user
        )

        val comment = Comment(
            id = 1L,
            user = user,
            text = "test",
            todo = todo
        )
        every { commentRepository.findByTodoIdAndId(todoId, commentId) } returns comment
        every { commentRepository.delete(any()) } just Runs

        // when
        commentService.deleteComment(todoId, commentId, userId)

        // then
        verify(exactly = 1) { commentRepository.delete(any()) }
    }
}