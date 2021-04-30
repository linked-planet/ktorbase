import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val jvmTarget: String by project

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("net.foragerr.jmeter") version "1.1.0-4.0"
}

val jmeterPlugins by configurations.creating {
    setTransitive(false)
}

val ktorVersion = "1.5.4"
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

    implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")

    // jmeter plugins to load
    jmeterPlugins("org.postgresql", "postgresql", "42.2.2")
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
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer::class.java) {
        resource = "reference.conf"
    }
}

task("updateBuildVersion") {
    val buildVersion = System.getProperty("buildVersion", "BUILD_VERSION")
    File("$projectDir/src/main/resources/application.conf")
        .apply {
            this.writeText(this.readText().replace("BUILD_VERSION", buildVersion))
        }
}

val initJmLibsTask = task("initJmLibs", Copy::class) {
    group = "unzip"
    from(jmeterPlugins.files)
    into("build/jmeter/lib/ext")
}

tasks.build.configure {
    dependsOn(initJmLibsTask)
}

jmeter {
    val env = System.getProperty("env", "local")
    println("### CONFIGURE JMETER for env: $env")
    jmTestFiles = listOf(file("src/test/resources/TemplateTest.jmx"))
    jmUserProperties = listOf("env=$env")
    enableReports = true
    enableExtendedReports = true
}
