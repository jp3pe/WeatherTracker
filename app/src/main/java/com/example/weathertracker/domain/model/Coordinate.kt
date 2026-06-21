package com.example.weathertracker.domain.model

/**
 * 위경도 좌표. GPS 좌표와 지역 좌표 모두 이 타입으로 표현한다.
 */
data class Coordinate(
    val latitude: Double,
    val longitude: Double,
)
