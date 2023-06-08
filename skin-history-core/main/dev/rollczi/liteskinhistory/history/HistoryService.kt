package dev.rollczi.liteskinhistory.history

import dev.rollczi.liteskinhistory.history.api.SkinHistoryResponse
import dev.rollczi.liteskinhistory.history.repository.HistoryRepository
import org.jetbrains.annotations.Blocking
import java.time.Instant

class HistoryService internal constructor(private val repository: HistoryRepository) {

    fun createHistory(username: String, skin: String, skinValue: String, instant: Instant = Instant.now()) {
        repository.createHistory(username, skin, skinValue, instant)
    }

    @Blocking
    fun findHistory(username: String, size: Int, page: Int): SkinHistoryResponse {
        return findHistory(username, HistoryRange(size, page))
    }

    @Blocking
    fun findHistory(username: String, historyRange: HistoryRange): SkinHistoryResponse {
        val records = repository.findHistory(username, historyRange)
        val total = repository.countHistory(username)

        return SkinHistoryResponse(
            records = records,
            total = total
        )
    }

}