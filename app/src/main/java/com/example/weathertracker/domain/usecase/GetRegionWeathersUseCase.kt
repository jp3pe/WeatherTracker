package com.example.weathertracker.domain.usecase

import com.example.weathertracker.core.result.Resource
import com.example.weathertracker.core.result.map
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.repository.WeatherRepository

/**
 * 전국 시/군/구 목록을 가져와 각 지역의 날씨를 조회한다.
 * (지역 목록 → 날씨 조회 2단계를 한 번에 묶어 ViewModel 을 단순화)
 */
class GetRegionWeathersUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(): Resource<List<RegionWeather>> {
        return when (val regions = repository.getRegions()) {
            is Resource.Error -> regions
            is Resource.Success -> repository.getWeather(regions.data).map { it }
        }
    }
}
