import org.gradle.util.GradleVersion

println("Gradle Version: " + GradleVersion.current().toString())
println("Java Version: " + JavaVersion.current().toString())

group = "com.linked-planet"
version = "0.1.0-SNAPSHOT"

ext.set("kotlinVersion", "1.7.20")
ext.set("jvmTarget", "1.8") // ktor prevents compile-time 11, see: https://youtrack.jetbrains.com/issue/KTOR-619

plugins {
    kotlin("multiplatform") version "1.7.20" apply false

    // provide & configure tasks: dependencyUpdates, useLatestVersions
    id("com.github.ben-manes.versions") version "0.44.0"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
}

allprojects {
    repositories {
        mavenCentral()
    }
}
