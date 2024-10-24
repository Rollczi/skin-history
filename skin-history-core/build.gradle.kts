import com.google.devtools.ksp.gradle.KspTask

plugins {
    `skin-history-kotlin`
    `skin-history-kotlin-unit-test`
    `skin-history-repositories`
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

dependencies {
    ksp("com.dzikoysk.sqiffy:sqiffy-symbol-processor:1.0.0-alpha.29")
    implementation("com.dzikoysk.sqiffy:sqiffy:1.0.0-alpha.60")
    api("net.dzikoysk:cdn:1.14.4")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.slf4j:slf4j-simple:2.0.6")

    testImplementation("org.testcontainers:mysql:1.18.1")
}

sourceSets.configureEach {
    kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin/")
}

tasks.withType<KspTask> {
    dependsOn("clean")
}