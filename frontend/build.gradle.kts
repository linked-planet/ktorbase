import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

val kotlinVersion: String by project
val devMode: Boolean = (ext.get("devMode") as String).toBoolean()
val reactVersion = "16.8.6"
println("Dev Mode: $devMode")

plugins {
    id("kotlin2js") version "1.3.50-eap-54"
    id("kotlin-dce-js") version "1.3.50-eap-54"
    id("org.jetbrains.kotlin.frontend") version "0.0.45"
}

dependencies {
    implementation(kotlin("stdlib-js", version = kotlinVersion))
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core-js", version = "1.1.1")
    implementation(project(":common"))
    implementation(group = "org.jetbrains", name = "kotlin-redux", version="4.0.0-pre.70-kotlin-1.3.21")
    implementation(group = "org.jetbrains", name = "kotlin-react-redux", version="5.0.7-pre.70-kotlin-1.3.21")
    compile(group = "org.jetbrains.kotlinx", name = "kotlinx-html-js", version = "0.6.12")
    compile(group = "org.jetbrains", name = "kotlin-react", version = "$reactVersion-pre.80-kotlin-1.3.41")
    compile(group = "org.jetbrains", name = "kotlin-react-dom", version = "$reactVersion-pre.80-kotlin-1.3.41")
}

kotlinFrontend {
    sourceMaps = true

    npm {
        replaceVersion("kotlin-js-library", "1.1.0")

        // React
        dependency("react", reactVersion)
        dependency("react-dom", reactVersion)
        dependency("core-js", "3")

        // Atlaskit
        dependency("@atlaskit/button", "^13.0.1")
        dependency("@atlaskit/checkbox", "^7.0.0")
        dependency("@atlaskit/dropdown-menu", "10.0.0")
        dependency("@atlaskit/flag", "^11.0.0")
        dependency("@atlaskit/icon", "^17.0.1")
        dependency("@atlaskit/textfield", "^2.0.0")
        dependency("@atlaskit/textarea", "^2.0.0")
        dependency("@atlaskit/select", "^9.0.1")
        dependency("@atlaskit/modal-dialog", "^9.0.0")
        dependency("@atlaskit/table-tree", "^8.0.5")
        dependency("@atlaskit/dynamic-table", "^13.7.7")
        dependency("@atlaskit/atlassian-navigation", "^0.10.13")
        dependency("@atlaskit/inline-edit", "^10.0.33")
        dependency("@atlaskit/logo", "^12.3.5")
        dependency("@atlaskit/page-layout", "0.8.0")
        dependency("@atlaskit/avatar", "19.0.0")
        dependency("@atlaskit/lozenge", "10.0.1")
        dependency("@atlaskit/popup", "0.6.0")
        dependency("@atlaskit/menu", "0.5.0")
        dependency("styled-components", "^3.5.0-0")

        // Redux
        dependency("redux", "4.0.5")
        dependency("react-redux", "7.2.1")

        // Misc
        dependency("uuid", "^3.3.2")

        devDependency("babel-loader")
        devDependency("style-loader")
        devDependency("css-loader")
        devDependency("sass-loader")
        devDependency("node-sass")
        devDependency("file-loader")
        devDependency("@babel/core")
    }

    configure<KotlinFrontendExtension> {
        bundle("webpack", delegateClosureOf<WebPackExtension> {
            publicPath = "/frontend/"
            port = 8080
            proxyUrl = "http://localhost:9090"
            mode = if (devMode) "development" else "production"
        })
    }
}
tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
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
    getByName("main").output.resourcesDir = file("build/js/resources")
}

tasks.withType<KotlinJsDce> {
    dceOptions.devMode = devMode
}
