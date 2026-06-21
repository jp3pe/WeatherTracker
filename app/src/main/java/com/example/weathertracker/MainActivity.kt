package com.example.weathertracker

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weathertracker.core.location.FusedLocationProvider
import com.example.weathertracker.presentation.weatherfilter.WeatherFilterScreen
import com.example.weathertracker.presentation.weatherfilter.WeatherFilterViewModel
import com.example.weathertracker.ui.theme.WeatherTrackerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherFilterViewModel by viewModels {
        val container = (application as WeatherTrackerApp).container
        WeatherFilterViewModel.Factory(
            getRegionWeathers = container.getRegionWeathersUseCase,
            filterRegions = container.filterRegionsByWeatherUseCase,
            locationProvider = FusedLocationProvider(applicationContext),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTrackerTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                ) { result ->
                    val granted = result.values.any { it }
                    viewModel.onPermissionResult(granted)
                }

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherFilterScreen(
                        uiState = uiState,
                        onToggleCondition = viewModel::toggleCondition,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
