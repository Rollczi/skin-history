package dev.rollczi.liteskinhistory.database

import dev.rollczi.liteskinhistory.config.ConfigFile

@ConfigFile("database.yml")
class SqiffyConfig {

    @JvmField var host: String = "localhost"
    @JvmField var port: Int = 3306
    @JvmField var database: String = "liteskinhistory"
    @JvmField var user: String = "root"
    @JvmField var password: String = ""
    @JvmField var otherParams: String = "?useSSL=false"
    @JvmField var driver: String = "com.mysql.cj.jdbc.Driver"
    @JvmField var driverUrl: String = "jdbc:mysql:"

    internal fun toUrl(): String {
        return "$driverUrl//$host:$port/$database$otherParams"
    }

}