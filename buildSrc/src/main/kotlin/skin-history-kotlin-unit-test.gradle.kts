plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets.test {
    kotlin.setSrcDirs(listOf("test"))
    resources.setSrcDirs(emptyList<String>())
}