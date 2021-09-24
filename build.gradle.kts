val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kodein_version: String by project
val mockk_version: String by project
val kotlinx_serialization_version: String by project
val kotlinx_datetime_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(15))
}

group = "nz.kirillov"
version = "0.0.1"
application {
    mainClass.set("nz.kirillov.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-controller-jvm:$kodein_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("io.mockk:mockk:$mockk_version")
}