package com.example.weathertracker.data.remote.api

/**
 * 외부 날씨 API 접속 정보.
 *
 * 키는 local.properties → BuildConfig → [AppContainer.fromBuildConfig] 로 주입된다.
 * KMA 는 Decoding 키 + Retrofit @Query 기본 인코딩을 사용한다.
 */
data class WeatherApiConfig(
    val provider: WeatherProvider,
    val kmaServiceKey: String,
    val owmApiKey: String,
) {
    enum class WeatherProvider { KMA, OPEN_WEATHER_MAP }

    companion object {
        const val KMA_BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
        const val OWM_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}
