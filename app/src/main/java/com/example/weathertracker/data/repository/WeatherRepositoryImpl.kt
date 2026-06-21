package com.example.weathertracker.data.repository

import com.example.weathertracker.core.result.Resource
import com.example.weathertracker.data.local.region.RegionDataSource
import com.example.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.example.weathertracker.domain.model.Region
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * [WeatherRepository] 구현.
 *
 * - 지역 목록: [RegionDataSource]
 * - 지역별 날씨: [WeatherRemoteDataSource] (KMA 또는 OWM 주입)
 *
 * 지역별 호출은 병렬로 수행하고, 일부 지역이 실패해도 나머지는 반환한다(부분 성공).
 * "어떤 API 를 쓰는지" 는 주입된 dataSource 가 캡슐화하므로 이 클래스는 모른다.
 */
class WeatherRepositoryImpl(
    private val regionDataSource: RegionDataSource,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : WeatherRepository {

    override suspend fun getRegions(): Resource<List<Region>> = withContext(ioDispatcher) {
        runCatching { regionDataSource.getRegions() }
            .fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { Resource.Error(it) },
            )
    }

    override suspend fun getWeather(regions: List<Region>): Resource<List<RegionWeather>> =
        withContext(ioDispatcher) {
            runCatching {
                coroutineScope {
                    regions
                        .map { region -> async { fetchOrNull(region) } }
                        .awaitAll()
                        .filterNotNull()
                }
            }.fold(
                onSuccess = { weathers ->
                    if (weathers.isEmpty() && regions.isNotEmpty()) {
                        Resource.Error(IllegalStateException("모든 지역의 날씨 조회에 실패했습니다."))
                    } else {
                        Resource.Success(weathers)
                    }
                },
                onFailure = { Resource.Error(it) },
            )
        }

    /** 단일 지역 실패가 전체를 무너뜨리지 않도록 개별 호출을 보호한다. */
    private suspend fun fetchOrNull(region: Region): RegionWeather? =
        runCatching { remoteDataSource.fetchWeather(region) }.getOrNull()
}
