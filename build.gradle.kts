import org.gradle.util.GradleVersion

println("Gradle Version: " + GradleVersion.current().toString())
println("Java Version: " + JavaVersion.current().toString())

group = "com.linked-planet"
version = "0.1.0-SNAPSHOT"

ext.set("kotlinVersion", "1.4.0")
ext.set("jvmTarget", "1.8") // ktor prevents compile-time 11, see: https://youtrack.jetbrains.com/issue/KTOR-619

plugins {
    kotlin("multiplatform") version "1.4.0" apply false
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.github.hierynomus.license-report") version "0.15.0"
    id("com.github.ben-manes.versions") version "0.21.0"
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    }
}
