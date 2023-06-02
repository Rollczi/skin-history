package dev.rollczi.liteskinhistory.history.api

data class SkinHistoryResponse(
    val records: List<SkinHistoryRecord>,
    val total: Long,
)