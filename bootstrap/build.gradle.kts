plugins {
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    compileOnly(rootProject.libs.paper.api)
}

tasks.processResources {
    filesMatching("paper-plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveBaseName.set("DeadHorizon")

    relocate("com.zaxxer.hikari", "gg.deadhorizon.libs.hikari")
    relocate("org.slf4j", "gg.deadhorizon.libs.slf4j")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}