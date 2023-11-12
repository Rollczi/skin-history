package dev.rollczi.liteskinhistory.database

import com.dzikoysk.sqiffy.*
import com.dzikoysk.sqiffy.changelog.SqiffyMetadataTable
import com.dzikoysk.sqiffy.shared.createHikariDataSource
import org.slf4j.event.Level

object SqiffyFactory {

    val METADATA_TABLE = SqiffyMetadataTable("lsk_metadata");

    fun createSqiffy(sqiffyConfig: SqiffyConfig): SqiffyDatabase {
        return Sqiffy.createDatabase(
            logger = object : SqiffyLogger { override fun log(level: Level, message: String) {} },
            dataSource = createHikariDataSource(
                driver = sqiffyConfig.driver,
                url = sqiffyConfig.toUrl(),
                username = sqiffyConfig.user,
                password = sqiffyConfig.password
            )
        )
    }

}