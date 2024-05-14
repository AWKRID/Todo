package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.todo.model.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TodoRepository : JpaRepository<Todo, Long> {
    override fun findAll(pageable: Pageable): Page<Todo>
    @Query("select t from Todo t join t.user u where u.name like concat('%',:name,'%')")
    fun findByName(@Param("name") name: String, pageable: Pageable): Page<Todo>

}