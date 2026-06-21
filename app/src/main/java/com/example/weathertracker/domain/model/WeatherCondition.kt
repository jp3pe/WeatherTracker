package com.example.weathertracker.domain.model

/**
 * 사용자가 선택/필터링할 수 있는 날씨 상태.
 *
 * 요구사항의 4가지(맑음/구름낌/비/눈)를 기본 제공하며,
 * 추후 항목 추가는 이 enum 에 상수를 추가하는 것만으로 확장된다.
 * 외부 API 의 코드 → 이 enum 매핑은 data 레이어의 Mapper 가 담당한다.
 */
enum class WeatherCondition(val label: String) {
    CLEAR("맑음"),
    CLOUDY("구름낌"),
    RAIN("비"),
    SNOW("눈"),

    /** 위 항목으로 분류되지 않은 상태. 필터 대상에서는 제외된다. */
    UNKNOWN("알 수 없음");

    /** 사용자가 필터로 선택 가능한 항목(UNKNOWN 제외). */
    companion object {
        val selectable: List<WeatherCondition> = entries.filter { it != UNKNOWN }
    }
}
