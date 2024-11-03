
plugins {
    `skin-history-java`
    `skin-history-repositories`

    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    api(project(":skin-history-core"))
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    compileOnly("net.skinsrestorer:skinsrestorer-api:15.4.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("LiteSkinHistory-velocity v${project.version}.jar")

    exclude(
        "org/intellij/**",
        "org/jetbrains/**",
    )

    val prefix = "dev.rollczi.liteskinhistory.libs"

    relocate("panda", "$prefix.org.panda")
    relocate("org.panda_lang", "$prefix.org.panda")
    relocate("dev.rollczi.litecommands", "$prefix.dev.rollczi.litecommands")
    relocate("dev.rollczi.liteskullapi", "$prefix.dev.rollczi.liteskullapi")
    relocate("net.dzikoysk", "$prefix.net.dzikoysk")
    relocate("com.dzikoysk", "$prefix.net.dzikoysk")
    relocate("net.kyori", "$prefix.net.kyori")
    relocate("com.google.gson", "$prefix.com.google.gson")
    relocate("com.fasterxml.jackson", "$prefix.com.fasterxml.jackson")
    relocate("org.slf4j", "$prefix.org.slf4j")
    relocate("io.leangen", "$prefix.io.leangen")
    relocate("google.protobuf", "$prefix.google.protobuf")
}
