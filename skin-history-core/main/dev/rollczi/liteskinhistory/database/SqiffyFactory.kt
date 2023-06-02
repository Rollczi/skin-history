package dev.rollczi.liteskinhistory.database

import com.dzikoysk.sqiffy.*
import com.dzikoysk.sqiffy.shared.createHikariDataSource

object SqiffyFactory {

    fun createSqiffy(sqiffyConfig: SqiffyConfig): SqiffyDatabase {
        return Sqiffy.createDatabase(
            logger = StdoutSqiffyLogger(),
            dataSource = createHikariDataSource(
                driver = sqiffyConfig.driver,
                url = sqiffyConfig.toUrl(),
                username = sqiffyConfig.user,
                password = sqiffyConfig.password
            )
        )
    }

}