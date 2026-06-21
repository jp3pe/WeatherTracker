package com.example.weathertracker.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * OpenWeatherMap 현재 날씨 응답 스키마.
 */
data class OwmCurrentWeatherResponse(
    @SerializedName("weather") val weather: List<OwmWeather> = emptyList(),
    @SerializedName("main") val main: OwmMain?,
    @SerializedName("dt") val dt: Long?,
    @SerializedName("name") val name: String?,
)

/**
 * @param id  날씨 상태 코드(2xx 뇌우, 3xx 이슬비, 5xx 비, 6xx 눈, 800 맑음, 80x 구름)
 */
data class OwmWeather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
)

data class OwmMain(
    @SerializedName("temp") val temp: Double,
)
