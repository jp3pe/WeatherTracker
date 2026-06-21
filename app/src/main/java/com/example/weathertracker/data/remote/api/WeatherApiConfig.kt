package com.example.weathertracker.data.remote.api

/**
 * 외부 날씨 API 접속 정보.
 *
 * 실제 키는 소스에 하드코딩하지 말고 local.properties → BuildConfig 로 주입하는 것을 권장한다.
 * 예) app/build.gradle.kts 에서
 *   buildConfigField("String", "KMA_SERVICE_KEY", "\"${'$'}{localProps["KMA_SERVICE_KEY"]}\"")
 * 후 AppContainer 에서 BuildConfig 값으로 이 객체를 생성한다.
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
