pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-dev") }
        maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers/") }
    }
}

rootProject.name = "ktorbase"

include("common", "backend", "frontend")
