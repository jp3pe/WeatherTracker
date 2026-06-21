package com.example.weathertracker.core.location

import com.example.weathertracker.domain.model.Coordinate

/**
 * 현재 사용자 위치(GPS) 조회 추상화.
 * 권한 요청은 UI 가 담당하고, 이 인터페이스는 좌표 반환만 책임진다.
 */
interface LocationProvider {
    /** 현재 위치를 반환. 권한 없음/실패 시 null. */
    suspend fun getCurrentLocation(): Coordinate?
}
