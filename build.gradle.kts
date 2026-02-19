plugins {
    java
}

group = "gg.deadhorizon"
version = "0.1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java-library")

    group = rootProject.group
    version = rootProject.version

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        testImplementation(platform(rootProject.libs.junit.bom))
        testImplementation(rootProject.libs.junit.jupiter)
        testRuntimeOnly(rootProject.libs.junit.launcher)
    }

    tasks.test {
        useJUnitPlatform()
    }
}