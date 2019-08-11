import com.palantir.gradle.docker.DockerExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("com.palantir.docker") version "0.21.0"
}

group = "pw.aru"
version = "1.2"

repositories {
    jcenter()
    maven { setUrl("https://jitpack.io") }
    maven { url = uri("https://dl.bintray.com/arudiscord/maven") }
    maven { url = uri("https://dl.bintray.com/arudiscord/kotlin") }
    mavenLocal()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile("pw.aru:aruCore:1.2")

    compile("org.kodein.di:kodein-di-generic-jvm:6.2.0")
    compile("pw.aru.libs:kodein-jit-bindings:2.2")

    compile("pw.aru.libs:properties:1.2")
    compile("pw.aru.libs:patreon-webhooks-java:1.1")

    //Jackson
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.8")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.8")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<ApplicationPluginConvention> {
    mainClassName = "pw.aru.auxiliary.AuxiliaryBootstrapKt"
}

configure<DockerExtension> {
    this.name = "adriantodt/aru-auxiliary:$version"

    val shadowJar by tasks.getting

    dependsOn(shadowJar)
    files(shadowJar.outputs)
    copySpec.from("runDir").into("run")

    buildArgs(mapOf("version" to version.toString()))
}
