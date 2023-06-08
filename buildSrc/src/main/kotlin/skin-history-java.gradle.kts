import gradle.kotlin.dsl.accessors._d135a9d1b0cd444ab2257975278a515f.implementation
import gradle.kotlin.dsl.accessors._d135a9d1b0cd444ab2257975278a515f.kotlin

plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
}

kotlin {
    jvmToolchain(17)
}


project.version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    main {
        java.setSrcDirs(listOf("main"))
        resources.setSrcDirs(emptyList<String>())
    }
    test {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(emptyList<String>())
    }
}