import org.jetbrains.kotlin.gradle.targets.js.nodejs.*

val buildVersion: String = System.getProperty("buildVersion", "BUILD_VERSION")
val kotlinVersion: String by project

plugins {
    kotlin("js")
}

kotlin {
    js {
        useCommonJs()
        browser {
            runTask {
                devServer = devServer?.copy(
                    proxy = mapOf(
                        "context" to arrayOf("/**/*"),
                        "target" to "http://localhost:9090"
                    )
                )
            }
            webpackTask {
                outputFileName = "${project.name}-$buildVersion.js"
            }
        }
        binaries.executable()
    }
}

val kotlinWrapperVersion = "pre.114-kotlin-$kotlinVersion"
val reduxVersion = "4.0.0"
val reactVersion = "16.13.1"
val reactReduxVersion = "5.0.7"
dependencies {
    implementation(kotlin("stdlib-js", kotlinVersion))
    implementation(project(":common"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core-js", "1.1.1")
    implementation("org.jetbrains.kotlinx", "kotlinx-html-js", "0.7.3")
    implementation("org.jetbrains", "kotlin-extensions", "1.0.1-$kotlinWrapperVersion")
    implementation("org.jetbrains", "kotlin-react", "$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains", "kotlin-react-dom", "$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains", "kotlin-redux", "$reduxVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains", "kotlin-react-redux", "$reactReduxVersion-$kotlinWrapperVersion")

    // React
    implementation(npm("react", reactVersion))
    implementation(npm("react-dom", reactVersion))
    implementation(npm("core-js", "3"))

    // Atlaskit
    implementation(npm("@atlaskit/button", "^13.0.1"))
    implementation(npm("@atlaskit/checkbox", "^7.0.0"))
    implementation(npm("@atlaskit/dropdown-menu", "10.0.0"))
    implementation(npm("@atlaskit/flag", "^11.0.0"))
    implementation(npm("@atlaskit/icon", "^17.0.1"))
    implementation(npm("@atlaskit/textfield", "^2.0.0"))
    implementation(npm("@atlaskit/textarea", "^2.0.0"))
    implementation(npm("@atlaskit/select", "^9.0.1"))
    implementation(npm("@atlaskit/modal-dialog", "^9.0.0"))
    implementation(npm("@atlaskit/table-tree", "^8.0.5"))
    implementation(npm("@atlaskit/dynamic-table", "^13.7.7"))
    implementation(npm("@atlaskit/atlassian-navigation", "^0.10.13"))
    implementation(npm("@atlaskit/inline-edit", "^10.0.33"))
    implementation(npm("@atlaskit/logo", "^12.3.5"))
    implementation(npm("@atlaskit/page-layout", "0.8.0"))
    implementation(npm("@atlaskit/avatar", "19.0.0"))
    implementation(npm("@atlaskit/lozenge", "10.0.1"))
    implementation(npm("@atlaskit/popup", "0.6.0"))
    implementation(npm("@atlaskit/menu", "0.5.0"))
    implementation(npm("styled-components", "^3.5.0-0"))

    // Redux
    implementation(npm("redux", reduxVersion))
    implementation(npm("react-redux", reactReduxVersion))

    // Misc
    implementation(npm("uuid", "^3.3.2"))

    implementation(devNpm("style-loader", "2.0.0"))
    implementation(devNpm("css-loader", "3.4.2"))
    implementation(devNpm("sass-loader", "10.1.0"))
    implementation(devNpm("node-sass", "4.14.1"))
    implementation(devNpm("file-loader", "6.2.0"))
    implementation(devNpm("@babel/core", "7.12.9"))
}

// without this, node will fail to execute in the Bitbucket Pipeline Build Container
rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.the<NodeJsRootExtension>().download = false
}
