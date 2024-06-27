package com.awkrid.todo.domain.todo.controller

import com.awkrid.todo.domain.todo.dto.CreateTodoRequest
import com.awkrid.todo.domain.todo.dto.TodoResponse
import com.awkrid.todo.domain.todo.dto.TodoResponseWithComments
import com.awkrid.todo.domain.todo.dto.UpdateTodoRequest
import com.awkrid.todo.domain.todo.service.TodoService
import com.awkrid.todo.domain.user.dto.UserResponse
import com.awkrid.todo.infra.security.jwt.JwtHelper
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val jwtHelper: JwtHelper,

    ) {
    @MockkBean
    private lateinit var todoService: TodoService
    private val accessToken = jwtHelper.generateAccessToken(
        subject = "1",
        name = "김민수",
        role = "USER"
    )

    @Test
    fun `getTodo - 성공 케이스`() {
        // given
        val todoId = 1L
        every { todoService.getTodoById(any()) } returns TodoResponseWithComments(
            id = 1L,
            title = "제목",
            description = "내용",
            date = LocalDateTime.of(2024, 6, 29, 10, 30),
            isDone = true,
            user = UserResponse(
                id = 1L,
                name = "김민수",
                role = "USER"
            ),
            tags = listOf("태그"),
            category = "카테고리",
            comments = listOf()
        )

        // when
        val resp = mockMvc.perform(
            get("/todos/$todoId")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn()

        // then
        resp.response.status shouldBe HttpStatus.OK.value()
    }

    @Test
    fun `createTodo - 성공 케이스`() {

        // given

        val requestBody = ObjectMapper().writeValueAsString(
            CreateTodoRequest(
                title = "test",
                description = "test",
                category = "category",
                tags = listOf("tag")
            )
        )
        every { todoService.createTodo(any(), any()) } returns TodoResponse(
            id = 1L,
            title = "test",
            description = "test",
            date = LocalDateTime.of(2024, 6, 29, 10, 30),
            isDone = false,
            user = UserResponse(
                id = 1L,
                name = "김민수",
                role = "USER"
            ),
            tags = listOf("tag"),
            category = "category"
        )

        // when
        val resp = mockMvc.perform(
            post("/todos")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()

        // then
        resp.response.status shouldBe HttpStatus.CREATED.value()

    }

    @Test
    fun `updateTodo - 성공 케이스`() {

        // given
        val todoId = 1L
        val requestBody = ObjectMapper().writeValueAsString(
            UpdateTodoRequest(
                title = "test",
                description = "test",
                category = "category",
                tags = listOf("tag"),
                isDone = true
            )
        )
        every { todoService.updateTodo(any(), any(), any()) } returns TodoResponse(
            id = 1L,
            title = "test",
            description = "test",
            date = LocalDateTime.of(2024, 6, 29, 10, 30),
            category = "category",
            tags = listOf("tag"),
            isDone = true,
            user = UserResponse(
                id = 1L,
                name = "김민수",
                role = "USER"
            )
        )

        // when
        val resp = mockMvc.perform(
            put("/todos/$todoId")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()

        // then
        resp.response.status shouldBe HttpStatus.OK.value()
    }

    @Test
    fun `deleteTodo - 성공 케이스`() {
        // given
        val todoId = 1L
        every { todoService.deleteTodo(any(), any()) } just Runs

        // when
        val resp = mockMvc.perform(
            delete("/todos/$todoId")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn()

        // then
        resp.response.status shouldBe HttpStatus.NO_CONTENT.value()
    }
}

