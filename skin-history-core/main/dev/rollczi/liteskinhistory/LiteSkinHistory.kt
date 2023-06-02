package dev.rollczi.liteskinhistory

import com.dzikoysk.sqiffy.SqiffyDatabase
import dev.rollczi.liteskinhistory.config.ConfigService
import dev.rollczi.liteskinhistory.database.SqiffyConfig
import dev.rollczi.liteskinhistory.database.SqiffyFactory
import dev.rollczi.liteskinhistory.history.HistoryFactory
import dev.rollczi.liteskinhistory.history.HistoryService
import java.io.File

class LiteSkinHistory(dataFolder: File) {

    private val sqiffyDatabase: SqiffyDatabase

    val configService: ConfigService
    val historyService: HistoryService

    init {
        configService = ConfigService(dataFolder)

        val sqiffyConfig = configService.load(SqiffyConfig::class.java)

        sqiffyDatabase = SqiffyFactory.createSqiffy(sqiffyConfig)
        historyService = HistoryFactory.sqiffy(sqiffyDatabase)
    }

    fun shutdown() {
        sqiffyDatabase.close()
    }

}