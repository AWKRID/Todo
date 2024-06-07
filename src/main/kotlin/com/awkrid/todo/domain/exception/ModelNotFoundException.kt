package com.awkrid.todo.domain.exception

data class ModelNotFoundException(val modelName: String, val id: Long?) :RuntimeException(
    "Model with name $modelName not found with id $id"
)

