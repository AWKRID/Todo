package com.awkrid.todo.domain.category.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id


@Entity
class Category(
    @Id
    @Column(name = "name")
    val name: String,
) {

}