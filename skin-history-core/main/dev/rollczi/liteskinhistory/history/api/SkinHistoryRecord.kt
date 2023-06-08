package dev.rollczi.liteskinhistory.history.api

import java.time.Instant

data class SkinHistoryRecord(
    val skin: String,
    val skinValue: String,
    val changedAt: Instant,
)