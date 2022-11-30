println("Gradle Version: " + GradleVersion.current().toString())
println("Java Version: " + JavaVersion.current().toString())

plugins {
    kotlin("multiplatform") version "1.7.22" apply false

    // derive gradle version from git tag
    id("pl.allegro.tech.build.axion-release") version "1.14.3"

    // provide & configure tasks: dependencyUpdates, useLatestVersions
    id("com.github.ben-manes.versions") version "0.44.0"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
}

group = "com.linked-planet"
version = scmVersion.version

ext.set("kotlinVersion", "1.7.22")

allprojects {
    repositories {
        mavenCentral()
    }
}
