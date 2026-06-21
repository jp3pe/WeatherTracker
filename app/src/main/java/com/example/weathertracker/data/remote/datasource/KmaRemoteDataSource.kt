package com.example.weathertracker.data.remote.datasource

import com.example.weathertracker.data.mapper.KmaWeatherMapper
import com.example.weathertracker.data.remote.api.KmaWeatherApi
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather

/**
 * 기상청 단기예보 기반 구현.
 * Api 호출 → Mapper 변환만 조립한다(비즈니스 규칙 없음).
 */
class KmaRemoteDataSource(
    private val api: KmaWeatherApi,
    private val mapper: KmaWeatherMapper,
    private val serviceKey: String,
    private val baseDateTimeProvider: KmaBaseDateTimeProvider = KmaBaseDateTimeProvider(),
) : WeatherRemoteDataSource {

    override suspend fun fetchWeather(region: Region): RegionWeather {
        val base = baseDateTimeProvider.current()
        val response = api.getVillageForecast(
            serviceKey = serviceKey,
            baseDate = base.baseDate,
            baseTime = base.baseTime,
            nx = region.grid.nx,
            ny = region.grid.ny,
        )
        return mapper.toRegionWeather(region, response)
    }
}
