import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.file.File.Companion.userHome

val kotlinVersion: String by project
val jvmTarget: String by project

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("net.foragerr.jmeter") version "1.1.0-4.0"
}

val ktorVersion = "1.5.4"
val jmeterPlugins: Configuration by configurations.creating {
    isTransitive = false
}
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
    file("src/main/resources/application.conf")
        .apply {
            writeText(readText().replace("BUILD_VERSION", buildVersion))
        }
}

/* -------------------------------------------------------------------------------------------
 * JMETER
 * -----------------------------------------------------------------------------------------*/
/* Copy jmeter plugins added via project dependencies into the proper folder
   **Example:**
   dependencies {
     ...
     jmeterPlugins("org.postgresql", "postgresql", "42.2.2")
   }
 */
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
    // env files for different test environments by convention stored at ~/.env/<project>
    val envFile = "$userHome/.env/${project.parent!!.name}/$env.env".takeIf { env != "local" }
        ?: file("src/test/resources/local.env").path

    println("### CONFIGURE JMETER ENVIRONMENT: $env - $envFile")
    assert(File(envFile).exists())

    jmTestFiles = file("src/test/resources").walkTopDown()
        .filter { it.extension == "jmx" }
        .onEach { println("- ${it.name}") }
        .toList()
    jmUserProperties = listOf("env=$env", "env_file=$envFile")
    enableReports = true
    enableExtendedReports = true
}
