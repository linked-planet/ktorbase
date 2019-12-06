import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val jvmTarget: String by project
val ktorVersion = "1.1.3"

plugins {
    kotlin("jvm") version "1.3.50-eap-54"
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

dependencies {
    implementation(kotlin("stdlib-jdk8", version = kotlinVersion))
    implementation(project(":common"))
    implementation(group = "io.ktor", name = "ktor-server-jetty", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-locations", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-html-builder", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-gson", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-client-apache", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-client-gson", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-client-auth-basic", version = ktorVersion)
    implementation(group = "io.ktor", name = "ktor-client-logging-jvm", version = ktorVersion)
    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
    implementation("com.link-time.ktor", "ktor-onelogin-saml", "1.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTarget
    kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental")
}

application {
    mainClassName = "io.ktor.server.jetty.EngineMain"
}

tasks.withType<ShadowJar> {
    baseName = project.name
    classifier = "all"
    // to make gradle work with embedded ktor jetty server
    // https://stackoverflow.com/questions/48636944/how-to-avoid-a-java-lang-exceptionininitializererror-when-trying-to-run-a-ktor-a/48698984#48698984
    mergeServiceFiles {
        setPath("META-INF/services")
        include("org.eclipse.jetty.http.HttpFieldPreEncoder")
    }
}
