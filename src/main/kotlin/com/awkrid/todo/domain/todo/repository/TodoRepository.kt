package com.awkrid.todo.domain.todo.repository

import com.awkrid.todo.domain.todo.model.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<Todo, Long> {

}