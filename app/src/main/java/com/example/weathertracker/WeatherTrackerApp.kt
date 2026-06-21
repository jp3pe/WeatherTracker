package com.example.weathertracker

import android.app.Application
import com.example.weathertracker.core.di.AppContainer

/**
 * Application 진입점. 앱 전역 의존성 그래프([AppContainer])를 보유한다.
 */
class WeatherTrackerApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
