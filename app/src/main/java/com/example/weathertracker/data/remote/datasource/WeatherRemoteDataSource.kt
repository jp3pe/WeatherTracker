package com.example.weathertracker.data.remote.datasource

import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather

/**
 * 단일 지역의 날씨를 외부 API 에서 가져오는 추상화.
 *
 * KMA/OWM 어느 쪽이든 이 인터페이스를 구현하므로, 상위(Repository)는
 * 제공자(provider)를 모른 채 동일한 방식으로 호출할 수 있다.
 * HTTP 호출(Api) 과 도메인 변환(Mapper) 의 조립만 담당한다.
 */
interface WeatherRemoteDataSource {
    suspend fun fetchWeather(region: Region): RegionWeather
}
