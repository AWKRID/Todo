package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.todo.model.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {
    override fun findAll(pageable: Pageable): Page<Todo>


}