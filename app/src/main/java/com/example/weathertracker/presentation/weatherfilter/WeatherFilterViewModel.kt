package com.example.weathertracker.presentation.weatherfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.core.location.LocationProvider
import com.example.weathertracker.core.result.Resource
import com.example.weathertracker.domain.model.RegionWeather
import com.example.weathertracker.domain.model.WeatherCondition
import com.example.weathertracker.domain.usecase.FilterRegionsByWeatherUseCase
import com.example.weathertracker.domain.usecase.GetRegionWeathersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 날씨 필터 화면 ViewModel.
 *
 * 역할 분담:
 *  - 전국 날씨 조회: [GetRegionWeathersUseCase]
 *  - 위치 조회: [LocationProvider]
 *  - 거리/조건 필터: [FilterRegionsByWeatherUseCase]
 * ViewModel 은 상태 보관과 흐름 제어만 하고, 계산 로직은 모두 위임한다.
 */
class WeatherFilterViewModel(
    private val getRegionWeathers: GetRegionWeathersUseCase,
    private val filterRegions: FilterRegionsByWeatherUseCase,
    private val locationProvider: LocationProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherFilterUiState())
    val uiState: StateFlow<WeatherFilterUiState> = _uiState.asStateFlow()

    /** 조회된 전국 날씨 캐시. 필터만 바꿀 때 재호출하지 않기 위해 보관. */
    private var cachedWeathers: List<RegionWeather> = emptyList()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted) }
        if (granted) refresh()
    }

    /** 위치 + 전국 날씨를 다시 불러오고 필터를 재계산한다. */
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val location = locationProvider.getCurrentLocation()
            when (val result = getRegionWeathers()) {
                is Resource.Success -> {
                    cachedWeathers = result.data
                    _uiState.update { it.copy(isLoading = false, userLocation = location) }
                    recomputeFilter()
                }

                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message ?: "날씨를 불러오지 못했습니다.")
                }
            }
        }
    }

    fun toggleCondition(condition: WeatherCondition) {
        _uiState.update { state ->
            val updated = state.selectedConditions.toMutableSet().apply {
                if (!add(condition)) remove(condition)
            }
            state.copy(selectedConditions = updated)
        }
        recomputeFilter()
    }

    fun setMaxRadiusKm(radiusKm: Double?) {
        _uiState.update { it.copy(maxRadiusKm = radiusKm) }
        recomputeFilter()
    }

    private fun recomputeFilter() {
        val state = _uiState.value
        val location = state.userLocation ?: return

        val results = filterRegions(
            userLocation = location,
            desiredConditions = state.selectedConditions,
            regionWeathers = cachedWeathers,
            maxRadiusKm = state.maxRadiusKm,
        )
        _uiState.update { it.copy(results = results) }
    }

    /** AppContainer 의존성을 주입하기 위한 Factory. */
    class Factory(
        private val getRegionWeathers: GetRegionWeathersUseCase,
        private val filterRegions: FilterRegionsByWeatherUseCase,
        private val locationProvider: LocationProvider,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherFilterViewModel(getRegionWeathers, filterRegions, locationProvider) as T
        }
    }
}
