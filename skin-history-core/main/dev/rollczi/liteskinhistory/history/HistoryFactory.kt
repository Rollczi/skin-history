package dev.rollczi.liteskinhistory.history

import com.dzikoysk.sqiffy.SqiffyDatabase
import dev.rollczi.liteskinhistory.history.repository.HistoryRepositorySqiffyImpl

internal object HistoryFactory {

    fun sqiffy(sqiffyDatabase: SqiffyDatabase): HistoryService {
        val historyRepository = HistoryRepositorySqiffyImpl(sqiffyDatabase)

        return HistoryService(historyRepository)
    }

}