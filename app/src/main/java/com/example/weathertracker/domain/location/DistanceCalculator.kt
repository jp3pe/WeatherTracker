package com.example.weathertracker.domain.location

import com.example.weathertracker.domain.model.Coordinate

/**
 * 두 좌표 사이의 거리 계산 추상화.
 *
 * 거리 계산 로직을 API 호출/필터링과 분리하기 위한 핵심 경계.
 * 구현을 교체(구면 근사 → 타원체 정밀 계산 등)해도 호출부는 영향받지 않는다.
 */
interface DistanceCalculator {
    /** [from] 에서 [to] 까지의 거리(km). */
    fun distanceKm(from: Coordinate, to: Coordinate): Double
}
