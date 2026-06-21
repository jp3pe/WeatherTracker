package com.example.weathertracker.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 기상청 단기예보(getVilageFcst) JSON 응답 스키마.
 * 도메인 모델과 분리된 순수 전송 객체이며, 변환은 Mapper 가 담당한다.
 */
data class KmaForecastResponse(
    @SerializedName("response") val response: KmaResponse,
)

data class KmaResponse(
    @SerializedName("header") val header: KmaHeader,
    @SerializedName("body") val body: KmaBody?,
)

data class KmaHeader(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String,
)

data class KmaBody(
    @SerializedName("items") val items: KmaItems?,
)

data class KmaItems(
    @SerializedName("item") val item: List<KmaForecastItem> = emptyList(),
)

/**
 * 예보 항목 하나.
 * @param category 예보 항목 코드 (SKY: 하늘상태, PTY: 강수형태, TMP: 1시간 기온 등)
 * @param fcstValue 예보 값
 */
data class KmaForecastItem(
    @SerializedName("category") val category: String,
    @SerializedName("fcstDate") val fcstDate: String,
    @SerializedName("fcstTime") val fcstTime: String,
    @SerializedName("fcstValue") val fcstValue: String,
    @SerializedName("nx") val nx: Int,
    @SerializedName("ny") val ny: Int,
)
