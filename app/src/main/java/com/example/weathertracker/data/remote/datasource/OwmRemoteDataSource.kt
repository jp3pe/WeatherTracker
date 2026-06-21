package com.example.weathertracker.data.remote.datasource

import com.example.weathertracker.data.mapper.OwmWeatherMapper
import com.example.weathertracker.data.remote.api.OpenWeatherMapApi
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather

/**
 * OpenWeatherMap 기반 구현.
 * KMA 와 동일한 [WeatherRemoteDataSource] 계약을 따르므로 상호 교체 가능하다.
 */
class OwmRemoteDataSource(
    private val api: OpenWeatherMapApi,
    private val mapper: OwmWeatherMapper,
    private val apiKey: String,
) : WeatherRemoteDataSource {

    override suspend fun fetchWeather(region: Region): RegionWeather {
        val response = api.getCurrentWeather(
            lat = region.coordinate.latitude,
            lon = region.coordinate.longitude,
            apiKey = apiKey,
        )
        return mapper.toRegionWeather(region, response)
    }
}
