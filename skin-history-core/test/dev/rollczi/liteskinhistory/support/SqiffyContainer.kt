package dev.rollczi.liteskinhistory.support

import com.dzikoysk.sqiffy.Slf4JSqiffyLogger
import com.dzikoysk.sqiffy.Sqiffy
import com.dzikoysk.sqiffy.SqiffyDatabase
import com.dzikoysk.sqiffy.StdoutSqiffyLogger
import com.dzikoysk.sqiffy.shared.createHikariDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.testcontainers.containers.MySQLContainer

class SqiffyContainer : MySQLContainer<SqiffyContainer>("mysql:8.0.33") {

    private val logger: Logger = LoggerFactory.getLogger("test")
    lateinit var sqiffyDatabase: SqiffyDatabase

    override fun start() {
        super.start()

        this.logger.info("Database: $jdbcUrl")

        sqiffyDatabase = Sqiffy.createDatabase(
            logger = StdoutSqiffyLogger(),
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