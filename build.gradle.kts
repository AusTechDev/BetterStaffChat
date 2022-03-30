import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.10" apply false
    id("io.freefair.lombok") version "6.0.0-m2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.austech"
version = "2.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(project(":common", "shadow"))
    implementation(project(":spigot", "shadow"))
    implementation(project(":bungeecord", "shadow"))
}

tasks {
    withType<ProcessResources> {
        expand("version" to project.version)
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "io.freefair.lombok")

    group = project.group
    version = project.version

    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://repo.alessiodp.com/releases/")
        }
    }

    dependencies {
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
        compileOnly("com.github.simplix-softworks:SimplixStorage:3.2.3")
        compileOnly("net.kyori:adventure-api:4.10.1")
        compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
        implementation("com.squareup.okio:okio:3.0.0") {
            exclude("org.jetbrains.kotlin")
        }
        compileOnly("net.dv8tion:JDA:5.0.0-alpha.9") {
            exclude("opus-java")
        }
    }

    sourceSets {
        main {
            java.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("net.kyori.adventure", "dev.austech.betterstaffchat.depend.adventure")
    relocate("de.leonhard.storage", "dev.austech.betterstaffchat.depend.storage")
    relocate("kotlin", "dev.austech.betterstaffchat.depend.kotlin")
    relocate("net.dv8tion.jda", "dev.austech.betterstaffchat.depend.discord")
    relocate("okio", "dev.austech.betterstaffchat.depend.okio")
}

task<Copy>("copyJars") {
    outputTasks().forEach { from(it) }
    rename("(.*)-all.jar", "$1.jar")
    into("jars")
}

fun outputTasks(): List<Task?> {
    return arrayOf("shadowJar", ":common:shadowJar", ":spigot:shadowJar", ":bungeecord:shadowJar").map {
        tasks.findByPath(it)
    }
}

task("cleanJars") {
    delete("jars")
}

tasks.named("clean") {
    dependsOn("cleanJars")
}

tasks.named("build") {
    dependsOn("shadowJar")
    dependsOn("copyJars")
}