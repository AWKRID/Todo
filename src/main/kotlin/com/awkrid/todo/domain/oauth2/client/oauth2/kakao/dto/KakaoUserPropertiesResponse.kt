package com.awkrid.todo.domain.oauth2.client.oauth2.kakao.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class KakaoUserPropertiesResponse(
    val nickname: String
) {
}