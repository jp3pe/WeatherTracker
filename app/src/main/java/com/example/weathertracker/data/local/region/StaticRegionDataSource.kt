package com.example.weathertracker.data.local.region

import com.example.weathertracker.domain.model.Coordinate
import com.example.weathertracker.domain.model.KmaGrid
import com.example.weathertracker.domain.model.Region

/**
 * 코드에 내장된 시/군/구 표본 데이터.
 *
 * 전국 전체(약 250여 개)는 기상청 "단기예보 격자 좌표(엑셀)" 를 변환해
 * app/src/main/assets/regions.csv 로 넣고 CSV 로더로 교체하는 것을 권장한다.
 * 여기서는 구조 검증용으로 주요 도시 일부만 포함한다.
 */
class StaticRegionDataSource : RegionDataSource {

    override suspend fun getRegions(): List<Region> = REGIONS

    private companion object {
        val REGIONS = listOf(
            Region("1111000000", "서울특별시 종로구", Coordinate(37.5729, 126.9794), KmaGrid(60, 127)),
            Region("1168000000", "서울특별시 강남구", Coordinate(37.5172, 127.0473), KmaGrid(61, 126)),
            Region("2611000000", "부산광역시 중구", Coordinate(35.1064, 129.0323), KmaGrid(97, 74)),
            Region("2711000000", "대구광역시 중구", Coordinate(35.8693, 128.6062), KmaGrid(89, 90)),
            Region("2811000000", "인천광역시 중구", Coordinate(37.4738, 126.6216), KmaGrid(54, 124)),
            Region("2911000000", "광주광역시 동구", Coordinate(35.1460, 126.9231), KmaGrid(60, 74)),
            Region("3011000000", "대전광역시 동구", Coordinate(36.3110, 127.4548), KmaGrid(68, 100)),
            Region("3111000000", "울산광역시 중구", Coordinate(35.5694, 129.3328), KmaGrid(102, 84)),
            Region("4111100000", "경기도 수원시 장안구", Coordinate(37.3043, 127.0103), KmaGrid(60, 121)),
            Region("4211100000", "강원특별자치도 춘천시", Coordinate(37.8813, 127.7300), KmaGrid(73, 134)),
            Region("4311100000", "충청북도 청주시 상당구", Coordinate(36.6360, 127.4890), KmaGrid(69, 106)),
            Region("4413100000", "충청남도 천안시 동남구", Coordinate(36.8065, 127.1522), KmaGrid(63, 110)),
            Region("4511100000", "전북특별자치도 전주시 완산구", Coordinate(35.8120, 127.1190), KmaGrid(63, 89)),
            Region("4611000000", "전라남도 목포시", Coordinate(34.8118, 126.3922), KmaGrid(50, 67)),
            Region("4711100000", "경상북도 포항시 남구", Coordinate(36.0190, 129.3435), KmaGrid(102, 94)),
            Region("4812500000", "경상남도 창원시 의창구", Coordinate(35.2540, 128.6400), KmaGrid(90, 77)),
            Region("5011000000", "제주특별자치도 제주시", Coordinate(33.4996, 126.5312), KmaGrid(53, 38)),
        )
    }
}
