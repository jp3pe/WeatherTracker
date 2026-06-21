package com.example.weathertracker.presentation.weatherfilter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weathertracker.domain.model.RegionWeatherWithDistance
import com.example.weathertracker.domain.model.WeatherCondition

/**
 * 날씨 필터 화면.
 * 상단: 희망 날씨 칩(다중 선택) / 본문: 사용자 위치 기준 가까운 순 결과 목록.
 */
@Composable
fun WeatherFilterScreen(
    uiState: WeatherFilterUiState,
    onToggleCondition: (WeatherCondition) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "보고 싶은 날씨", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        ConditionChips(
            selectable = uiState.selectableConditions,
            selected = uiState.selectedConditions,
            onToggle = onToggleCondition,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        )

        when {
            uiState.isLoading -> CenteredBox { CircularProgressIndicator() }
            uiState.errorMessage != null -> CenteredBox { Text(uiState.errorMessage) }
            !uiState.locationPermissionGranted ->
                CenteredBox { Text("위치 권한을 허용하면 주변 날씨를 보여드립니다.", textAlign = TextAlign.Center) }
            uiState.results.isEmpty() ->
                CenteredBox { Text("조건에 맞는 지역이 없습니다.") }
            else -> ResultList(results = uiState.results, modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConditionChips(
    selectable: List<WeatherCondition>,
    selected: Set<WeatherCondition>,
    onToggle: (WeatherCondition) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        selectable.forEach { condition ->
            val isSelected = condition in selected
            FilterChip(
                selected = isSelected,
                onClick = { onToggle(condition) },
                label = { Text(condition.label) },
                colors = FilterChipDefaults.filterChipColors(),
            )
        }
    }
}

@Composable
private fun ResultList(
    results: List<RegionWeatherWithDistance>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(results) { item -> ResultRow(item) }
    }
}

@Composable
private fun ResultRow(item: RegionWeatherWithDistance) {
    val rw = item.regionWeather
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = rw.region.name, style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
                Text(text = "${rw.condition.label}${rw.temperatureC?.let { " · ${it}°C" } ?: ""}")
            }
            Text(text = "%.1f km".format(item.distanceKm))
        }
    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) { content() }
}
