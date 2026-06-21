package com.example.weathertracker.domain.repository

import com.example.weathertracker.core.result.Resource
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather

/**
 * 날씨 데이터 접근 추상화.
 *
 * 도메인은 "어떤 API(KMA/OWM)를 쓰는지" 알지 못한다.
 * 구현체(data 레이어)가 RemoteDataSource 선택/매핑을 캡슐화한다.
 */
interface WeatherRepository {

    /** 전국 시/군/구 목록(좌표/격자 포함)을 반환한다. */
    suspend fun getRegions(): Resource<List<Region>>

    /**
     * 주어진 지역들의 현재(또는 가장 가까운 예보 시점) 날씨를 조회한다.
     * 일부 지역 실패가 전체 실패가 되지 않도록 구현은 부분 성공을 허용할 수 있다.
     */
    suspend fun getWeather(regions: List<Region>): Resource<List<RegionWeather>>
}
