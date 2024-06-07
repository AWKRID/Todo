package com.awkrid.todo.domain.oauth2.client.oauth2.kakao.dto

import com.awkrid.todo.domain.oauth2.OAuth2Provider
import com.awkrid.todo.domain.oauth2.client.oauth2.OAuth2LoginUserInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
class KakaoLoginUserInfoResponse(
    id: Long,
    properties: KakaoUserPropertiesResponse
) : OAuth2LoginUserInfo(
    provider = OAuth2Provider.KAKAO,
    id = id.toString(),
    name = properties.nickname
)