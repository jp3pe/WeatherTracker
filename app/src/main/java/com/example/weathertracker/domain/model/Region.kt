package com.example.weathertracker.domain.model

/**
 * 전국 시/군/구 단위 행정구역.
 *
 * @param code     행정구역 식별자(법정동코드 등 고유 키)
 * @param name     표시 이름 (예: "서울특별시 종로구")
 * @param coordinate 행정구역 대표 위경도. 거리 계산과 OpenWeatherMap 호출에 사용.
 * @param grid     기상청(KMA) 격자 좌표. KMA 단기예보 호출에 사용.
 */
data class Region(
    val code: String,
    val name: String,
    val coordinate: Coordinate,
    val grid: KmaGrid,
)

/** 기상청 단기예보용 격자 좌표(nx, ny). */
data class KmaGrid(
    val nx: Int,
    val ny: Int,
)
