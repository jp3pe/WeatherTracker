package com.example.weathertracker.domain.usecase

import com.example.weathertracker.domain.location.HaversineDistanceCalculator
import com.example.weathertracker.domain.model.Coordinate
import com.example.weathertracker.domain.model.KmaGrid
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterRegionsByWeatherUseCaseTest {

    private val useCase = FilterRegionsByWeatherUseCase(HaversineDistanceCalculator())

    private fun region(name: String, lat: Double, lon: Double) =
        Region(code = name, name = name, coordinate = Coordinate(lat, lon), grid = KmaGrid(0, 0))

    private fun weather(name: String, lat: Double, lon: Double, condition: WeatherCondition) =
        RegionWeather(region = region(name, lat, lon), condition = condition)

    private val user = Coordinate(37.5665, 126.9780) // 서울

    @Test
    fun `filters by desired condition`() {
        val data = listOf(
            weather("A", 37.6, 127.0, WeatherCondition.CLEAR),
            weather("B", 37.7, 127.1, WeatherCondition.RAIN),
            weather("C", 37.8, 127.2, WeatherCondition.SNOW),
        )

        val result = useCase(
            userLocation = user,
            desiredConditions = setOf(WeatherCondition.CLEAR, WeatherCondition.SNOW),
            regionWeathers = data,
        )

        assertEquals(setOf("A", "C"), result.map { it.regionWeather.region.name }.toSet())
    }

    @Test
    fun `sorts by distance ascending`() {
        val data = listOf(
            weather("Far", 35.1796, 129.0756, WeatherCondition.CLEAR),  // 부산
            weather("Near", 37.6, 127.0, WeatherCondition.CLEAR),        // 서울 근처
        )

        val result = useCase(user, setOf(WeatherCondition.CLEAR), data)

        assertEquals("Near", result.first().regionWeather.region.name)
        assertTrue(result[0].distanceKm <= result[1].distanceKm)
    }

    @Test
    fun `respects max radius`() {
        val data = listOf(
            weather("Busan", 35.1796, 129.0756, WeatherCondition.CLEAR), // 약 325km
            weather("Seoul", 37.6, 127.0, WeatherCondition.CLEAR),       // 수 km
        )

        val result = useCase(user, setOf(WeatherCondition.CLEAR), data, maxRadiusKm = 100.0)

        assertEquals(listOf("Seoul"), result.map { it.regionWeather.region.name })
    }

    @Test
    fun `unknown condition is always excluded`() {
        val data = listOf(weather("X", 37.6, 127.0, WeatherCondition.UNKNOWN))

        // 선택이 비어 있어도(전체 통과) UNKNOWN 은 제외
        val result = useCase(user, emptySet(), data)

        assertTrue(result.isEmpty())
    }
}
