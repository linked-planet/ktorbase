import org.gradle.util.GradleVersion

println("Gradle Version: " + GradleVersion.current().toString())

group = "com.linktime"
version = "0.1.0-SNAPSHOT"

ext.set("kotlinVersion", "1.3.50-eap-54")
ext.set("jvmTarget", "1.8")

plugins {
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.github.hierynomus.license-report") version "0.15.0"
    id("com.github.ben-manes.versions") version "0.21.0"
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlinx.html") }
        maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}
