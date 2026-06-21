package com.example.weathertracker.data.mapper

import com.example.weathertracker.data.remote.dto.KmaForecastItem
import com.example.weathertracker.data.remote.dto.KmaForecastResponse
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.model.WeatherCondition

/**
 * 기상청 단기예보 응답 → 도메인 [RegionWeather] 변환.
 *
 * 가장 이른 예보 시각의 SKY(하늘상태)/PTY(강수형태)/TMP(기온)를 읽어
 * 단일 [WeatherCondition] 으로 환원한다. (강수형태가 하늘상태보다 우선)
 */
class KmaWeatherMapper {

    fun toRegionWeather(region: Region, response: KmaForecastResponse): RegionWeather {
        val items = response.response.body?.items?.item.orEmpty()

        // 가장 이른 예보 시점 선택
        val earliestKey = items
            .map { it.fcstDate + it.fcstTime }
            .minOrNull()

        val current = items.filter { it.fcstDate + it.fcstTime == earliestKey }
        val byCategory = current.associateBy({ it.category }, { it.fcstValue })

        val condition = resolveCondition(
            sky = byCategory[CATEGORY_SKY],
            pty = byCategory[CATEGORY_PTY],
        )

        return RegionWeather(
            region = region,
            condition = condition,
            temperatureC = byCategory[CATEGORY_TMP]?.toDoubleOrNull(),
            observedAtMillis = null,
        )
    }

    private fun resolveCondition(sky: String?, pty: String?): WeatherCondition {
        // 강수형태(PTY) 우선
        when (pty) {
            PTY_RAIN, PTY_RAIN_SNOW, PTY_SHOWER -> return WeatherCondition.RAIN
            PTY_SNOW -> return WeatherCondition.SNOW
        }
        // 강수 없음 → 하늘상태(SKY)
        return when (sky) {
            SKY_CLEAR -> WeatherCondition.CLEAR
            SKY_MOSTLY_CLOUDY, SKY_CLOUDY -> WeatherCondition.CLOUDY
            else -> WeatherCondition.UNKNOWN
        }
    }

    private companion object {
        const val CATEGORY_SKY = "SKY"
        const val CATEGORY_PTY = "PTY"
        const val CATEGORY_TMP = "TMP"

        // SKY: 하늘상태
        const val SKY_CLEAR = "1"          // 맑음
        const val SKY_MOSTLY_CLOUDY = "3"  // 구름많음
        const val SKY_CLOUDY = "4"         // 흐림

        // PTY: 강수형태
        const val PTY_RAIN = "1"           // 비
        const val PTY_RAIN_SNOW = "2"      // 비/눈
        const val PTY_SNOW = "3"           // 눈
        const val PTY_SHOWER = "4"         // 소나기
    }
}

/** 단위 테스트 편의를 위해 노출하는 항목 접근 헬퍼. */
internal fun List<KmaForecastItem>.valueOf(category: String): String? =
    firstOrNull { it.category == category }?.fcstValue
