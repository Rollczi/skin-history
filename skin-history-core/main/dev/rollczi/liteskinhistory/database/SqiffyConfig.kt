package dev.rollczi.liteskinhistory.database

import dev.rollczi.liteskinhistory.config.ConfigFile

@ConfigFile("database.yml")
data class SqiffyConfig(
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = "liteskinhistory",
    val user: String = "root",
    val password: String = "",
    val otherParams: String = "?useSSL=false",
    val driver: String = "com.mysql.jdbc.Driver",
    val driverUrl: String = "jdbc:mysql:"
) {

    internal fun toUrl(): String {
        return "$driverUrl//$host:$port/$database$otherParams"
    }

}