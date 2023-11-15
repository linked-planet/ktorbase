import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val ktorVersion = "1.5.4"
val log4jVersion = "2.19.0"
dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(project(":common"))

    implementation("io.ktor", "ktor-server-jetty", ktorVersion)
    implementation("io.ktor", "ktor-locations", ktorVersion)
    implementation("io.ktor", "ktor-html-builder", ktorVersion)
    implementation("io.ktor", "ktor-gson", ktorVersion)
    implementation("io.ktor", "ktor-client-apache", ktorVersion)
    implementation("io.ktor", "ktor-client-gson", ktorVersion)
    implementation("io.ktor", "ktor-client-auth-jvm", ktorVersion)
    implementation("io.ktor", "ktor-client-logging-jvm", ktorVersion)
    implementation("com.link-time.ktor", "ktor-onelogin-saml", "1.2.0-ktor-1.4.2")

    implementation("org.apache.logging.log4j", "log4j-api", log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-core", log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)

    testImplementation(kotlin("test"))
    testImplementation("io.rest-assured", "rest-assured", "5.3.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental")
}

application {
    mainClass.set("io.ktor.server.jetty.EngineMain")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set(project.name)
    archiveClassifier.set("all")
    // to make gradle work with embedded ktor jetty server
    // https://stackoverflow.com/questions/48636944/how-to-avoid-a-java-lang-exceptionininitializererror-when-trying-to-run-a-ktor-a/48698984#48698984
    mergeServiceFiles {
        setPath("META-INF/services")
        include("org.eclipse.jetty.http.HttpFieldPreEncoder")
    }
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer::class.java) {
        resource = "reference.conf"
    }
}

task("updateBuildVersion") {
    val buildVersion = System.getProperty("buildVersion", "BUILD_VERSION")
    file("src/main/resources/application.conf")
        .apply {
            writeText(readText().replace("BUILD_VERSION", buildVersion))
        }
}

sourceSets {
    create("integration") {
        kotlin {
            compileClasspath += main.get().output + configurations.testRuntimeClasspath.get()
            runtimeClasspath += output + compileClasspath
        }
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Run all integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integration"].output.classesDirs
    classpath = sourceSets["integration"].runtimeClasspath
    mustRunAfter(tasks["test"])
}
