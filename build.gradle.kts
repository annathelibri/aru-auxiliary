import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("com.github.ben-manes.versions") version "0.21.0"
}

group = "pw.aru"
version = "1.1"

repositories {
    jcenter()
    maven { setUrl("https://jitpack.io") }
    maven { url = uri("https://dl.bintray.com/arudiscord/maven") }
    maven { url = uri("https://dl.bintray.com/arudiscord/kotlin") }
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("pw.aru:aruCore:1.0")

    implementation("org.kodein.di:kodein-di-generic-jvm:6.2.0")
    implementation("pw.aru.libs:kodein-jit-bindings:2.2")

    implementation("pw.aru.libs:properties:1.2")
    implementation("pw.aru.libs:patreon-webhooks-java:1.1")

    //Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.8")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<ApplicationPluginConvention> {
    mainClassName = "pw.aru.auxiliary.AuxiliaryBootstrapKt"
}
