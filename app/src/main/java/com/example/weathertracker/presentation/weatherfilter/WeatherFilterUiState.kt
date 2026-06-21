package com.example.weathertracker.presentation.weatherfilter

import com.example.weathertracker.domain.model.Coordinate
import com.example.weathertracker.domain.model.RegionWeatherWithDistance
import com.example.weathertracker.domain.model.WeatherCondition

/**
 * 날씨 필터 화면 상태.
 *
 * @param userLocation        현재 사용자 GPS 좌표(없으면 null)
 * @param selectedConditions  사용자가 선택한 희망 날씨(비어 있으면 전체)
 * @param maxRadiusKm         최대 반경(km). null 이면 제한 없음.
 * @param results             필터/정렬된 결과(가까운 순)
 */
data class WeatherFilterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val locationPermissionGranted: Boolean = false,
    val userLocation: Coordinate? = null,
    val selectedConditions: Set<WeatherCondition> = emptySet(),
    val maxRadiusKm: Double? = DEFAULT_RADIUS_KM,
    val results: List<RegionWeatherWithDistance> = emptyList(),
) {
    val selectableConditions: List<WeatherCondition> = WeatherCondition.selectable

    companion object {
        const val DEFAULT_RADIUS_KM = 100.0
    }
}
