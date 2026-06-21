package com.example.weathertracker.data.local.region

import com.example.weathertracker.domain.model.Region

/**
 * 전국 시/군/구 목록 제공 추상화.
 *
 * 현재는 정적 데이터([StaticRegionDataSource])를 사용하지만,
 * 추후 assets CSV/JSON 로딩이나 Room DB 캐시로 교체해도 상위는 영향이 없다.
 */
interface RegionDataSource {
    suspend fun getRegions(): List<Region>
}
