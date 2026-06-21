package com.example.weathertracker.data.remote.api

import com.example.weathertracker.data.remote.dto.OwmCurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * OpenWeatherMap 현재 날씨 조회 (Current Weather Data API).
 */
interface OpenWeatherMapApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr",
    ): OwmCurrentWeatherResponse
}
