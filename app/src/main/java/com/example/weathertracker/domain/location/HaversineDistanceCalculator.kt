package com.example.weathertracker.domain.location

import com.example.weathertracker.domain.model.Coordinate
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Haversine 공식 기반 거리 계산 구현.
 *
 * 지구를 반지름 [EARTH_RADIUS_KM] 의 완전한 구로 근사한다.
 * 시/군/구 단위 필터링에는 충분한 정밀도이며, 외부 의존성이 없는 순수 로직이라
 * 단위 테스트가 용이하다.
 */
class HaversineDistanceCalculator : DistanceCalculator {

    override fun distanceKm(from: Coordinate, to: Coordinate): Double {
        val dLat = Math.toRadians(to.latitude - from.latitude)
        val dLon = Math.toRadians(to.longitude - from.longitude)

        val lat1 = Math.toRadians(from.latitude)
        val lat2 = Math.toRadians(to.latitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
            sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }

    private companion object {
        const val EARTH_RADIUS_KM = 6371.0
    }
}
