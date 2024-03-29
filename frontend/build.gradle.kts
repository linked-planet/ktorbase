val buildVersion: String = System.getProperty("buildVersion", "BUILD_VERSION")
val kotlinVersion: String by project

plugins {
    kotlin("js")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    js {
        useCommonJs()
        browser {
            webpackTask {
                outputFileName = "${project.name}-$buildVersion.js"
            }
            dceTask {
                keep("frontend.main")
            }
        }
        binaries.executable()
    }
}

dependencies {
    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation(project(":common"))
    implementation("com.linked-planet.ui", "ui-kit-lib", "0.11.0")

    implementation(npm("@atlaskit/menu", "0.5.0"))
}
