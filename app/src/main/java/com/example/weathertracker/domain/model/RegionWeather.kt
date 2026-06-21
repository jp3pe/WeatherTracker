package com.example.weathertracker.domain.model

/**
 * 특정 지역의 날씨 조회 결과(도메인 모델).
 * 외부 API DTO 와 분리되어 있으며, Mapper 를 통해 변환되어 들어온다.
 *
 * @param temperatureC 기온(섭씨). 제공되지 않으면 null.
 * @param observedAtMillis 관측/예보 기준 시각(epoch millis). 알 수 없으면 null.
 */
data class RegionWeather(
    val region: Region,
    val condition: WeatherCondition,
    val temperatureC: Double? = null,
    val observedAtMillis: Long? = null,
)

/**
 * 필터 결과 한 건. 사용자 위치로부터의 거리를 함께 담는다.
 * 거리 계산 자체는 DistanceCalculator 가 수행하고, 이 모델은 결과만 보관한다.
 */
data class RegionWeatherWithDistance(
    val regionWeather: RegionWeather,
    val distanceKm: Double,
)
