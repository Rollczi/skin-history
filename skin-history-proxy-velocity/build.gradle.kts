plugins {
    `skin-history-java`
    `skin-history-repositories`

    id("com.github.johnrengelman.shadow") version "8.0.0"
}

dependencies {
    api(project(":skin-history-core"))
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    implementation("net.skinsrestorer:skinsrestorer-api:14.2.10")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName.set("LiteSkinHistory-velocity v${project.version}.jar")

    mergeServiceFiles()
    minimize()

    val prefix = "dev.rollczi.liteskinhistory.velocity.libs."

    relocate("panda", "$prefix.org.panda")
    relocate("org.panda_lang", "$prefix.org.panda")
    relocate("dev.rollczi.litecommands", "$prefix.dev.rollczi")
    relocate("net.dzikoysk", "$prefix.net.dzikoysk")
    relocate("net.kyori", "$prefix.net.kyori")
}
