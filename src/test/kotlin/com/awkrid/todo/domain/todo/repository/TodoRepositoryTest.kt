package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.todo.dto.TodoFilter
import com.awkrid.todo.domain.todo.model.Todo
import com.awkrid.todo.domain.user.model.User
import com.awkrid.todo.domain.user.model.UserRole
import com.awkrid.todo.domain.user.repository.UserRepository
import com.awkrid.todo.infra.querydsl.QueryDslConfig
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class])
@ActiveProfiles("test")
class TodoRepositoryTest @Autowired constructor(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
) {

    @Test
    fun `filter가 없는 경우 전체 데이터 조회되는지 확인`() {
        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }
        val filter = TodoFilter()

        // when
        val result1 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), filter)
        val result2 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 8), filter)
        val result3 = todoRepository.findByPageableAndFilters(PageRequest.of(1, 8), filter)
        val result4 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 15), filter)

        // then
        result1.content.size shouldBe 10
        result2.content.size shouldBe 8
        result3.content.size shouldBe 2
        result4.content.size shouldBe 10
    }

    @Test
    fun `개별 필터에 의해 동작하는지 확인`() {
        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }
        // when
        val result1 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(title = "밥"))
        val result2 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(title = "친구"))

        val result3 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(tag = "숙제"))
        val result4 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(tag = "탕후루"))

        val result5 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(category = "친구"))
        val result6 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(category = "스터디"))

        val result7 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(isDone = false))
        val result8 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(isDone = true))

        // then
        result1.content.size shouldBe 6
        result2.content.size shouldBe 2
        result3.content.size shouldBe 2
        result4.content.size shouldBe 1
        result5.content.size shouldBe 2
        result6.content.size shouldBe 2
        result7.content.size shouldBe 5
        result8.content.size shouldBe 5
    }

    @Test
    fun `2개 이상의 filter 조건에 대해서 동작하는지 확인`() {
        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }

        // when
        val result1 = todoRepository.findByPageableAndFilters(
            pageable = PageRequest.of(0, 10),
            TodoFilter(title = "밥", tag = "저녁")
        )
        val result2 = todoRepository.findByPageableAndFilters(
            pageable = PageRequest.of(0, 10),
            TodoFilter(category = "친구", tag = "탕후루")
        )
        val result3 = todoRepository.findByPageableAndFilters(
            pageable = PageRequest.of(0, 10),
            TodoFilter(category = "스터디", tag = "숙제", isDone = true)
        )

        // then
        result1.content.size shouldBe 3
        result2.content.size shouldBe 1
        result3.content.size shouldBe 1

    }

    @Test
    fun `검색 결과가 0건인 경우 결과 확인`() {

        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }
        // when
        val result1 = todoRepository.findByPageableAndFilters(PageRequest.of(0, 10), TodoFilter(title = "뷁"))

        // then
        result1.content.size shouldBe 0
    }

    @Test
    fun `조회된 결과가 10개, PageSize가 6일 때 0페이지 확인`() {
        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }
        // when
        val result = todoRepository.findByPageableAndFilters(PageRequest.of(0, 6), TodoFilter())

        // then
        result.content.size shouldBe 6
        result.isLast shouldBe false
        result.totalPages shouldBe 2
        result.number shouldBe 0
        result.totalElements shouldBe 10
    }

    @Test
    fun `조회된 결과가 10개, PageSize가 6일 때 1페이지 확인`() {
        // given
        userRepository.saveAndFlush(user)
            .let { todoRepository.saveAllAndFlush(todoList(it)) }
        // when
        val result = todoRepository.findByPageableAndFilters(PageRequest.of(1, 6), TodoFilter())

        // then
        result.content.size shouldBe 4
        result.isLast shouldBe true
        result.totalPages shouldBe 2
        result.number shouldBe 1
        result.totalElements shouldBe 10
    }

    companion object {
        private val user = User(name = "test", role = UserRole.USER)
        fun todoList(user: User): List<Todo> {
            return listOf(
                Todo(
                    title = "밥약속1",
                    description = "밥 약속입니다",
                    isDone = false,
                    tags = "#밥#저녁#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "밥약속2",
                    description = "밥 약속입니다",
                    isDone = true,
                    tags = "#밥#점심#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "밥약속3",
                    description = "밥 약속입니다",
                    isDone = false,
                    tags = "#밥#저녁#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "밥약속4",
                    description = "밥 약속입니다",
                    isDone = true,
                    tags = "#밥#점심#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "밥약속5",
                    description = "밥 약속입니다",
                    isDone = false,
                    tags = "#밥#저녁#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "밥약속6",
                    description = "밥 약속입니다",
                    isDone = true,
                    tags = "#밥#점심#",
                    category = "식사",
                    user = user
                ),
                Todo(
                    title = "공부1",
                    description = "숙제하기",
                    isDone = false,
                    tags = "#숙제#공부#",
                    category = "스터디",
                    user = user
                ),
                Todo(
                    title = "친구만나기1",
                    description = "친구랑 약속",
                    isDone = false,
                    tags = "#노래방#탕후루#",
                    category = "친구",
                    user = user
                ),
                Todo(
                    title = "공부2",
                    description = "숙제하기",
                    isDone = true,
                    tags = "#숙제#공부#",
                    category = "스터디",
                    user = user
                ),
                Todo(
                    title = "친구만나기2",
                    description = "친구랑 게임하기",
                    isDone = true,
                    tags = "#게임#피시방#",
                    category = "친구",
                    user = user
                ),
            )
        }
    }


}