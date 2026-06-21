package com.example.weathertracker.core.di

import com.example.weathertracker.data.local.region.RegionDataSource
import com.example.weathertracker.data.local.region.StaticRegionDataSource
import com.example.weathertracker.data.mapper.KmaWeatherMapper
import com.example.weathertracker.data.mapper.OwmWeatherMapper
import com.example.weathertracker.data.remote.api.KmaWeatherApi
import com.example.weathertracker.data.remote.api.OpenWeatherMapApi
import com.example.weathertracker.data.remote.api.WeatherApiConfig
import com.example.weathertracker.data.remote.api.WeatherApiConfig.WeatherProvider
import com.example.weathertracker.data.remote.datasource.KmaRemoteDataSource
import com.example.weathertracker.data.remote.datasource.OwmRemoteDataSource
import com.example.weathertracker.data.remote.datasource.WeatherRemoteDataSource
import com.example.weathertracker.data.repository.WeatherRepositoryImpl
import com.example.weathertracker.domain.location.DistanceCalculator
import com.example.weathertracker.domain.location.HaversineDistanceCalculator
import com.example.weathertracker.domain.repository.WeatherRepository
import com.example.weathertracker.domain.usecase.FilterRegionsByWeatherUseCase
import com.example.weathertracker.domain.usecase.GetRegionWeathersUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 수동 DI 컨테이너(의존성 그래프 조립 지점).
 *
 * Hilt 없이 추가 Gradle 플러그인 없이 동작하며, 추후 Hilt 모듈로 그대로 옮기기 쉽도록
 * 각 의존성을 명시적으로 구성한다. provider 교체(KMA ↔ OWM)는 [config] 한 곳에서 결정된다.
 */
class AppContainer(
    private val config: WeatherApiConfig = DEFAULT_CONFIG,
) {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC },
            )
            .build()
    }

    private val regionDataSource: RegionDataSource by lazy { StaticRegionDataSource() }

    private val remoteDataSource: WeatherRemoteDataSource by lazy {
        when (config.provider) {
            WeatherProvider.KMA -> KmaRemoteDataSource(
                api = retrofit(WeatherApiConfig.KMA_BASE_URL).create(KmaWeatherApi::class.java),
                mapper = KmaWeatherMapper(),
                serviceKey = config.kmaServiceKey,
            )

            WeatherProvider.OPEN_WEATHER_MAP -> OwmRemoteDataSource(
                api = retrofit(WeatherApiConfig.OWM_BASE_URL).create(OpenWeatherMapApi::class.java),
                mapper = OwmWeatherMapper(),
                apiKey = config.owmApiKey,
            )
        }
    }

    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(
            regionDataSource = regionDataSource,
            remoteDataSource = remoteDataSource,
        )
    }

    val distanceCalculator: DistanceCalculator by lazy { HaversineDistanceCalculator() }

    val getRegionWeathersUseCase: GetRegionWeathersUseCase by lazy {
        GetRegionWeathersUseCase(weatherRepository)
    }

    val filterRegionsByWeatherUseCase: FilterRegionsByWeatherUseCase by lazy {
        FilterRegionsByWeatherUseCase(distanceCalculator)
    }

    private fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private companion object {
        // TODO: 실제 키는 local.properties → BuildConfig 로 주입하세요.
        val DEFAULT_CONFIG = WeatherApiConfig(
            provider = WeatherProvider.KMA,
            kmaServiceKey = "REPLACE_WITH_KMA_SERVICE_KEY",
            owmApiKey = "REPLACE_WITH_OWM_API_KEY",
        )
    }
}
