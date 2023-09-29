rootProject.name = "some-command"
pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version "1.4.1"
        id("org.jetbrains.kotlin.jvm") version "1.9.10"
    }

}
