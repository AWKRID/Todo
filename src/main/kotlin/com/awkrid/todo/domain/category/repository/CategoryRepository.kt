package com.awkrid.todo.domain.category.repository

import com.awkrid.todo.domain.category.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, String> {
    fun findByName(name: String): Category?
    fun existsByName(name: String): Boolean
}