package dev.rollczi.liteskinhistory.history.api

import java.time.Instant

data class SkinHistoryRecord(
    val skin: String,
    val changedAt: Instant,
)