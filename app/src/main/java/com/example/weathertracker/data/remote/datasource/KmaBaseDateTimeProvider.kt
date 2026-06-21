package com.example.weathertracker.data.remote.datasource

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 기상청 단기예보의 base_date / base_time 계산기.
 *
 * 단기예보는 하루 8회(02,05,08,11,14,17,20,23시) 발표되며 발표 후 약 10분 뒤 제공된다.
 * 현재 시각 기준으로 사용 가능한 가장 최신 발표 시각을 산출한다.
 * (시간 계산 로직을 DataSource 본체에서 분리해 테스트 가능하게 함)
 */
class KmaBaseDateTimeProvider(
    private val clock: () -> LocalDateTime = { LocalDateTime.now() },
) {
    data class BaseDateTime(val baseDate: String, val baseTime: String)

    fun current(): BaseDateTime {
        val now = clock()
        // 발표 후 제공 지연(약 10분) 보정
        val adjusted = now.minusMinutes(PROVISION_DELAY_MINUTES)

        val availableHour = ANNOUNCE_HOURS.lastOrNull { it <= adjusted.hour }

        val base = if (availableHour == null) {
            // 자정~02:10 이전: 전날 마지막 발표(23시) 사용
            adjusted.minusDays(1).withHour(ANNOUNCE_HOURS.last())
        } else {
            adjusted.withHour(availableHour)
        }

        return BaseDateTime(
            baseDate = base.format(DATE_FORMAT),
            baseTime = "%02d00".format(base.hour),
        )
    }

    private companion object {
        const val PROVISION_DELAY_MINUTES = 10L
        val ANNOUNCE_HOURS = listOf(2, 5, 8, 11, 14, 17, 20, 23)
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
