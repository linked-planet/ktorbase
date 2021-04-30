val kotlinVersion: String by project
val jvmTarget: String by project
val copyJvmTarget = jvmTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js {
        browser {
            testTask {
                testLogging {
                    showExceptions = true
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    showCauses = true
                    showStackTraces = true
                }
            }
        }
        nodejs {
            testTask {
                testLogging {
                    showExceptions = true
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    showCauses = true
                    showStackTraces = true
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common", version = kotlinVersion))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8", version = kotlinVersion))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js", version = kotlinVersion))
            }
        }
    }
}
