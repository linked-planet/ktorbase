val kotlinVersion: String by project
val jvmTarget: String by project
val copyJvmTarget = jvmTarget

plugins {
    kotlin("multiplatform") version "1.3.50-eap-54"
}

kotlin {
    jvm {
        compilations.getting {
            kotlinOptions {
                jvmTarget = copyJvmTarget
            }
        }
    }

    js {
        configure(listOf(compilations["main"])) {
            kotlinOptions {
                metaInfo = true
                outputFile = "${project.buildDir.path}/js/${project.name}.js"
                sourceMap = true
                sourceMapEmbedSources = "always"
                moduleKind = "commonjs"
                main = "call"
            }
        }
    }

    sourceSets {
        getting {
            dependencies {
                implementation(kotlin("stdlib-common", version = kotlinVersion))
            }
        }
        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8", version = kotlinVersion))
            }
        }
        js().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-js", version = kotlinVersion))
            }
        }
    }
}
