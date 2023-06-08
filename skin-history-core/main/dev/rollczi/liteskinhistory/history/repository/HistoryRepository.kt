package dev.rollczi.liteskinhistory.history.repository

import dev.rollczi.liteskinhistory.history.HistoryRange
import dev.rollczi.liteskinhistory.history.api.SkinHistoryRecord
import java.time.Instant

internal interface HistoryRepository {

    fun createHistory(username: String, skinName: String, skinValue: String, changedAt: Instant): SkinHistory

    fun findHistory(username: String, historyRange: HistoryRange): List<SkinHistoryRecord>

    fun countHistory(username: String): Long

}