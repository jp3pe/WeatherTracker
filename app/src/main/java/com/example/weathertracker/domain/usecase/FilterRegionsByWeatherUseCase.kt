package com.example.weathertracker.domain.usecase

import com.example.weathertracker.domain.location.DistanceCalculator
import com.example.weathertracker.domain.model.Coordinate
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.model.RegionWeatherWithDistance
import com.example.weathertracker.domain.model.WeatherCondition

/**
 * 사용자 GPS 좌표 기준으로, 사용자가 희망하는 날씨를 가진 지역들을 필터링한다.
 *
 * 거리 계산은 [DistanceCalculator] 에 위임하고, 이 유즈케이스는
 * "원하는 날씨 매칭 → 거리 부여 → 반경 필터 → 거리순 정렬" 흐름만 담당한다.
 * (API 호출과도, 구체적 거리 공식과도 분리된 순수 로직)
 */
class FilterRegionsByWeatherUseCase(
    private val distanceCalculator: DistanceCalculator,
) {
    /**
     * @param userLocation       사용자 GPS 좌표
     * @param desiredConditions  사용자가 보고 싶은 날씨 집합(비어 있으면 전체 통과)
     * @param regionWeathers     조회된 전국 지역 날씨
     * @param maxRadiusKm        최대 반경(km). null 이면 거리 제한 없음.
     * @return 조건/반경을 만족하는 지역을 사용자와 가까운 순으로 정렬한 결과
     */
    operator fun invoke(
        userLocation: Coordinate,
        desiredConditions: Set<WeatherCondition>,
        regionWeathers: List<RegionWeather>,
        maxRadiusKm: Double? = null,
    ): List<RegionWeatherWithDistance> {
        return regionWeathers.asSequence()
            .filter { matchesDesiredCondition(it.condition, desiredConditions) }
            .map { rw ->
                RegionWeatherWithDistance(
                    regionWeather = rw,
                    distanceKm = distanceCalculator.distanceKm(userLocation, rw.region.coordinate),
                )
            }
            .filter { maxRadiusKm == null || it.distanceKm <= maxRadiusKm }
            .sortedBy { it.distanceKm }
            .toList()
    }

    private fun matchesDesiredCondition(
        condition: WeatherCondition,
        desired: Set<WeatherCondition>,
    ): Boolean {
        if (condition == WeatherCondition.UNKNOWN) return false
        // 선택이 없으면 (UNKNOWN 제외) 모든 날씨를 통과시킨다.
        return desired.isEmpty() || condition in desired
    }
}
