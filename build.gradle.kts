plugins {
    kotlin("multiplatform") version "1.7.22" apply false

    // derive gradle version from git tag
    id("pl.allegro.tech.build.axion-release") version "1.15.0"

    // provide & configure dependencyUpdates
    id("com.github.ben-manes.versions") version "0.46.0"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
}

group = "com.linked-planet"
version = scmVersion.version

ext.set("kotlinVersion", "1.7.22")

allprojects {
    repositories {
        mavenCentral()
    }
}
