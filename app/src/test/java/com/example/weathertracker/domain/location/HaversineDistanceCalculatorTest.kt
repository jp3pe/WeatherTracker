package com.example.weathertracker.domain.location

import com.example.weathertracker.domain.model.Coordinate
import org.junit.Assert.assertEquals
import org.junit.Test

class HaversineDistanceCalculatorTest {

    private val calculator = HaversineDistanceCalculator()

    @Test
    fun `same point returns zero`() {
        val seoul = Coordinate(37.5665, 126.9780)
        assertEquals(0.0, calculator.distanceKm(seoul, seoul), 0.0001)
    }

    @Test
    fun `seoul to busan is about 325km`() {
        val seoul = Coordinate(37.5665, 126.9780)
        val busan = Coordinate(35.1796, 129.0756)
        // 실제 직선거리 약 325km. 구면 근사 허용 오차 ±5km.
        assertEquals(325.0, calculator.distanceKm(seoul, busan), 5.0)
    }
}
