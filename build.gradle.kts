plugins {
    application
    kotlin("jvm") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val ktor_version = "2.3.4"

repositories {
    maven("https://repo.danubetech.com/repository/maven-public/")
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation("com.github.TBD54566975:web5-kt:PR84-SNAPSHOT")
    implementation("io.ktor:ktor-client-core:$ktor_version") // This is needed for web5-kt dependency

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
