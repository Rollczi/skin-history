package dev.rollczi.liteskinhistory.config

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigFile(val fileName: String)
