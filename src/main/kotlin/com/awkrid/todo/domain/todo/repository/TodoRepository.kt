package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.comment.model.QComment
import com.awkrid.todo.domain.todo.dto.TodoFilter
import com.awkrid.todo.domain.todo.model.QTodo
import com.awkrid.todo.domain.todo.model.Todo
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TodoRepository : JpaRepository<Todo, Long>, TodoQueryDslRepository {

}

interface TodoQueryDslRepository {
    fun findByPageableAndFilters(
        pageable: Pageable,
        filter: TodoFilter,
    ): Page<Todo>
}

class TodoQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TodoQueryDslRepository {
    private val todo = QTodo.todo
    private val comment = QComment.comment
    override fun findByPageableAndFilters(
        pageable: Pageable,
        filter: TodoFilter
    ): Page<Todo> {
        val totalCount = jpaQueryFactory
            .select(todo.count())
            .from(todo)
            .where(
                titleLike(filter.title),
                containTag(filter.tag),
                categoryEq(filter.category),
                isDoneEq(filter.isDone),
                withInDays(filter.daysAgo)
            )
            .fetchOne() ?: 0L

        val query = jpaQueryFactory
            .selectFrom(todo)
            .where(
                titleLike(filter.title),
                containTag(filter.tag),
                categoryEq(filter.category),
                isDoneEq(filter.isDone),
                withInDays(filter.daysAgo)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val contents = query.fetch()
        return PageImpl(contents, pageable, totalCount)
    }

    private fun titleLike(title: String?): BooleanExpression? {
        return if (title.isNullOrEmpty()) null else todo.title.contains(title)
    }

    private fun containTag(tag: String?): BooleanExpression? {
        return if (tag.isNullOrEmpty()) null else todo.tags.contains("#$tag#")
    }

    private fun categoryEq(category: String?): BooleanExpression? {
        return if (category.isNullOrEmpty()) null else todo.category.eq(category)
    }

    private fun isDoneEq(isDone: Boolean?): BooleanExpression? {
        return if (isDone == null) null else todo.isDone.eq(isDone)
    }

    private fun withInDays(daysAgo: Long?): BooleanExpression? {
        val now = LocalDateTime.now()
        return if (daysAgo == null) null else todo.date.between(now.minusDays(daysAgo), now)
    }

}
