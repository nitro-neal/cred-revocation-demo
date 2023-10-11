plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven("https://repo.danubetech.com/repository/maven-public/")
}

dependencies {
    implementation("com.github.TBD54566975:web5-kt:PR76-SNAPSHOT")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}