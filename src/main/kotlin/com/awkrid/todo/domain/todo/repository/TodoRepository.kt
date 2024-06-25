package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.comment.model.QComment
import com.awkrid.todo.domain.todo.model.QTodo
import com.awkrid.todo.domain.todo.model.Todo
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<Todo, Long>, TodoQueryDslRepository {
    override fun findAll(pageable: Pageable): Page<Todo>


    @Query("select t from Todo t join t.user u where u.name like concat('%',:name,'%')")
    fun findByName(@Param("name") name: String, pageable: Pageable): Page<Todo>

}

interface TodoQueryDslRepository {

}

class TodoQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TodoQueryDslRepository {
    private val todo = QTodo.todo
    private val comment = QComment.comment

}
