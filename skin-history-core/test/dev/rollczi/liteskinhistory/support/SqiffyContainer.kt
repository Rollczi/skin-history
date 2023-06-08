package dev.rollczi.liteskinhistory.support

import com.dzikoysk.sqiffy.Sqiffy
import com.dzikoysk.sqiffy.SqiffyDatabase
import com.dzikoysk.sqiffy.StdoutSqiffyLogger
import com.dzikoysk.sqiffy.shared.createHikariDataSource
import org.slf4j.event.Level
import org.testcontainers.containers.MySQLContainer

class SqiffyContainer : MySQLContainer<SqiffyContainer>("mysql:8.0.33") {

    private val logger = StdoutSqiffyLogger()
    lateinit var sqiffyDatabase: SqiffyDatabase

    override fun start() {
        super.start()

        logger.log(Level.INFO, "Database: $jdbcUrl")

        sqiffyDatabase = Sqiffy.createDatabase(
            logger = logger,
            dataSource = createHikariDataSource(
                driver = this.driverClassName,
                url = this.jdbcUrl,
                username = this.username,
                password = this.password,
            )
        )
    }

    override fun stop() {
        super.stop()
        sqiffyDatabase.close()
    }

}