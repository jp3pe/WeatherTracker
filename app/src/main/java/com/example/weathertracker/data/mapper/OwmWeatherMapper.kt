package com.example.weathertracker.data.mapper

import com.example.weathertracker.data.remote.dto.OwmCurrentWeatherResponse
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.model.WeatherCondition

/**
 * OpenWeatherMap 응답 → 도메인 [RegionWeather] 변환.
 * weather.id 코드 그룹을 [WeatherCondition] 으로 매핑한다.
 */
class OwmWeatherMapper {

    fun toRegionWeather(region: Region, response: OwmCurrentWeatherResponse): RegionWeather {
        val code = response.weather.firstOrNull()?.id

        return RegionWeather(
            region = region,
            condition = resolveCondition(code),
            temperatureC = response.main?.temp,
            observedAtMillis = response.dt?.let { it * 1000 },
        )
    }

    private fun resolveCondition(code: Int?): WeatherCondition = when (code) {
        null -> WeatherCondition.UNKNOWN
        CLEAR_CODE -> WeatherCondition.CLEAR
        in CLOUDS_RANGE -> WeatherCondition.CLOUDY
        in SNOW_RANGE -> WeatherCondition.SNOW
        // 뇌우(2xx)/이슬비(3xx)/비(5xx)는 모두 '비'로 분류
        in THUNDERSTORM_RANGE, in DRIZZLE_RANGE, in RAIN_RANGE -> WeatherCondition.RAIN
        else -> WeatherCondition.UNKNOWN
    }

    private companion object {
        const val CLEAR_CODE = 800
        val CLOUDS_RANGE = 801..804
        val THUNDERSTORM_RANGE = 200..299
        val DRIZZLE_RANGE = 300..399
        val RAIN_RANGE = 500..599
        val SNOW_RANGE = 600..699
    }
}
