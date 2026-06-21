package com.example.weathertracker.data.remote.api

import com.example.weathertracker.data.remote.dto.KmaForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 기상청 단기예보 조회 서비스 (getVilageFcst).
 *
 * 순수 HTTP 계약만 정의한다. base_date/base_time 계산, 격자 좌표, 응답 해석 등은
 * 상위(DataSource/Mapper)의 책임이며 이 인터페이스에는 비즈니스 로직이 없다.
 */
interface KmaWeatherApi {

    @GET("getVilageFcst")
    suspend fun getVillageForecast(
        @Query("serviceKey") serviceKey: String,
        @Query("dataType") dataType: String = "JSON",
        @Query("numOfRows") numOfRows: Int = 1000,
        @Query("pageNo") pageNo: Int = 1,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
    ): KmaForecastResponse
}
