package com.awkrid.todo.domain.todo.service

import com.awkrid.todo.domain.category.model.Category
import com.awkrid.todo.domain.category.repository.CategoryRepository
import com.awkrid.todo.domain.comment.repository.CommentRepository
import com.awkrid.todo.domain.exception.ModelNotFoundException
import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
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


class TodoServiceTest {
    private val userRepository = mockk<UserRepository>()
    private val todoRepository = mockk<TodoRepository>()
    private val commentRepository = mockk<CommentRepository>()
    private val categoryRepository = mockk<CategoryRepository>()
    private val todoService = TodoServiceImpl(
        userRepository = userRepository,
        todoRepository = todoRepository,
        categoryRepository = categoryRepository,
        commentRepository = commentRepository
    )

    @Test
    fun `getTodoById - 존재하지 않는 id에 대해서 ModelNotFoundException이 발생하는지 확인`() {
        //given
        val todoId = 1L
        every { todoRepository.findByIdOrNull(todoId) } returns null

        //when,then
        shouldThrow<ModelNotFoundException> {
            todoService.getTodoById(todoId)
        }
    }

    @Test
    fun `createTodo - 존재하지 않는 user에 대해 ModelNotFoundException이 발생하는지 확인`() {
        // given
        val userId = 1L
        val req = CreateTodoRequest(
            title = "제목",
            description = "내용",
            category = "카테고리",
            tags = listOf("태그1")
        )
        every { userRepository.findByIdOrNull(userId) } returns null

        shouldThrow<ModelNotFoundException> {
            todoService.createTodo(req, userId)
        }
    }

    @Test
    fun `createTodo - 존재하지 않는 category에 대해 ModelNotFoundException이 발생하는지 확인`() {
        // given
        val userId = 1L
        val req = CreateTodoRequest(
            title = "제목",
            description = "내용",
            category = "카테고리",
            tags = listOf("태그1")
        )
        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )
        every { userRepository.findByIdOrNull(userId) } returns user
        every { categoryRepository.findByName(any()) } returns null

        shouldThrow<ModelNotFoundException> {
            todoService.createTodo(req, userId)
        }
    }

    @Test
    fun `createTodo - 성공 케이스`() {
        // given
        val userId = 1L
        val req = CreateTodoRequest(
            title = "제목",
            description = "내용",
            category = "카테고리",
            tags = listOf("태그1")
        )
        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )
        val category = Category(
            name = "카테고리"
        )
        every { userRepository.findByIdOrNull(userId) } returns user
        every { categoryRepository.findByName(any()) } returns category
        every { todoRepository.save(any()) } returns Todo(
            id = 1L,
            title = req.title,
            description = req.description,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            user = user,
            category = req.category
        )
        // when
        val resp = todoService.createTodo(req, userId)

        // then
        verify(exactly = 1) { todoRepository.save(any()) }
        resp.title shouldBe "제목"
        resp.category shouldBe "카테고리"
        resp.description shouldBe "내용"

    }

    @Test
    fun `updateTodo - Todo가 없는 경우 ModelNotFoundException 발생하는지 확인`() {

        // given
        val todoId = 1L
        val userId = 1L
        val req = UpdateTodoRequest(
            title = "제목",
            description = "내용",
            isDone = true,
            category = "카테고리",
            tags = listOf("태그")
        )
        every { todoRepository.findByIdOrNull(todoId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            todoService.updateTodo(todoId, req, userId)
        }
    }

    @Test
    fun `updateTodo - Category가 없는 경우 ModelNotFoundException 발생하는지 확인`() {

        // given
        val todoId = 1L
        val userId = 1L
        val req = UpdateTodoRequest(
            title = "제목",
            description = "내용",
            isDone = true,
            category = "카테고리",
            tags = listOf("태그")
        )
        val user = User(
            id = 1L,
            name = "김민수",
            role = UserRole.USER
        )
        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            isDone = true,
            category = "카테고리",
            tags = "#태그#",
            user = user
        )
        every { todoRepository.findByIdOrNull(todoId) } returns todo
        every { categoryRepository.findByName(any()) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            todoService.updateTodo(todoId, req, userId)
        }
    }

    @Test
    fun `updateTodo - userId가 다른 경우 InvalidCredentialException 발생하는지 확인`() {
        // given
        val todoId = 1L
        val userId = 1L
        val req = UpdateTodoRequest(
            title = "제목",
            description = "내용",
            isDone = true,
            category = "카테고리",
            tags = listOf("태그")
        )
        val user = User(
            id = 2L,
            name = "김민수",
            role = UserRole.USER
        )
        val todo = Todo(
            id = 1L,
            title = "제목",
            description = "내용",
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            isDone = true,
            category = "카테고리",
            tags = "#태그#",
            user = user
        )
        val category = Category("카테고리")
        every { todoRepository.findByIdOrNull(todoId) } returns todo
        every { categoryRepository.findByName(any()) } returns category
        // when, then
        shouldThrow<InvalidCredentialException> {
            todoService.updateTodo(todoId, req, userId)
        }
    }

    @Test
    fun `updateTodo - 성공케이스`() {
        // given
        val todoId = 1L
        val userId = 1L
        val req = UpdateTodoRequest(
            title = "제목 after",
            description = "내용 after",
            isDone = true,
            category = "카테고리 after",
            tags = listOf("태그 after")
        )
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
        val todoAfter = Todo(
            id = 1L,
            title = "제목 after",
            description = "내용 after",
            isDone = true,
            date = LocalDateTime.of(2024, 6, 29, 15, 54),
            category = "카테고리 after",
            tags = "#태그after#",
            user = user
        )
        val category = Category("카테고리")
        every { todoRepository.findByIdOrNull(todoId) } returns todo
        every { categoryRepository.findByName(any()) } returns category
        every { todoRepository.save(any()) } returns todoAfter

        // when
        val resp = todoService.updateTodo(todoId, req, userId)

        //then
        verify(exactly = 1) { todoRepository.save(any()) }
        resp.title shouldBe "제목 after"
        resp.description shouldBe "내용 after"
        resp.isDone shouldBe true
        resp.category shouldBe "카테고리 after"
        resp.tags shouldBe listOf("태그after")
    }

    @Test
    fun `deleteTodo - todo가 없는 경우 ModelNotFoundException 발생하는지 확인`() {
        // given
        val todoId = 1L
        val userId = 1L

        every { todoRepository.findByIdOrNull(todoId) } returns null

        // when, then
        shouldThrow<ModelNotFoundException> {
            todoService.deleteTodo(todoId, userId)
        }
    }

    @Test
    fun `deleteTodo - userId가 다른 경우 InvalidCredentialException 발생하는지 확인`() {
        val todoId = 1L
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

        every { todoRepository.findByIdOrNull(todoId) } returns todo

        // when, then
        shouldThrow<InvalidCredentialException> {
            todoService.deleteTodo(todoId, userId)
        }
    }

    @Test
    fun `deleteTodo - 성공 케이스`() {
        val todoId = 1L
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
        every { todoRepository.delete(any()) } just Runs

        // when
        todoService.deleteTodo(todoId, userId)

        // then
        verify(exactly = 1) { todoRepository.delete(any()) }
    }
}