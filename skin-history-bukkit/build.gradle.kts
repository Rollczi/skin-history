plugins {
    `skin-history-java`
    `skin-history-repositories`

    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

dependencies {
    api(project(":skin-history-core"))

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    implementation("dev.rollczi:liteskullapi:1.3.0")
    implementation("dev.rollczi.litecommands:bukkit-adventure:2.8.8")
    implementation("dev.triumphteam:triumph-gui:3.1.5")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
    }
}

bukkit {
    main = "dev.rollczi.liteskinhistory.bukkit.LiteSkinHistoryBukkit"
    apiVersion = "1.13"
    prefix = "LiteSkinHistory"
    author = "Rollczi"
    name = "LiteSkinHistory"
    version = "${project.version}"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("LiteSkinHistory-bukkit v${project.version}.jar")

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