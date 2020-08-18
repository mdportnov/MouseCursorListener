import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.50"
    application
//    java
//    id("com.zyxist.chainsaw") version "0.3.1"
//    id("org.beryx.jlink") version "2.21.2"
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("org.beryx.runtime") version "1.11.2"
}

javafx {
    version = "11.0.2"
    modules=listOf("javafx.controls", "javafx.base", "javafx.graphics",
            "javafx.fxml", "javafx.media", "javafx.swing", "javafx.web")
}

//val currentOS = org.gradle.internal.os.OperatingSystem.current()
//val platform = when {
//    currentOS.isWindows -> "win"
//    currentOS.isLinux -> "linux"
//    currentOS.isMacOsX -> "mac"
//    else -> throw GradleException("Unsupported operating system: $currentOS")
//}

//val javaFxVersion = 11

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

application {
    mainClassName = "com.example.demo.ApplicationRunner"
    applicationDefaultJvmArgs = listOf(
            "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:2.0.0-SNAPSHOT")
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.github.thomasnield:rxkotlinfx:2.2.2")
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation("com.google.code.gson:gson:2.8.6")
//    implementation("org.openjfx:javafx-base:${javaFxVersion}:${platform}")
//    implementation("org.openjfx:javafx-controls:${javaFxVersion}:${platform}") {
//        exclude(module = "javafx-graphics")
//    }
//    implementation("org.openjfx:javafx-fxml:${javaFxVersion}:${platform}") {
//        exclude(module = "javafx-controls")
//    }
//    implementation("org.openjfx:javafx-graphics:${javaFxVersion}:${platform}") {
//        exclude(module = "javafx-base")
//    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        kotlinOptions.freeCompilerArgs += "-Xinline-classes"
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.example.demo.ApplicationRunner"
    }
}

runtime {
    imageZip.set(project.file("${project.buildDir}/mouseListener.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    modules.set(listOf("java.desktop", "jdk.unsupported", "java.scripting", "java.logging", "java.xml", "java.sql"))
}

//jlink {
//    launcher {
//        name = "tornadoApp"
//    }
//    addExtraDependencies("javafx")
//    imageZip.set(project.file("${project.buildDir}/image-zip/hello-image.zip"))
//}

//val shadowJar: ShadowJar by tasks
//shadowJar.apply {
//    manifest.attributes.apply {
//        put("Implementation-Title", "TornadoShadowFX")
//        put("Implementation-Version", "5.2.0")
//        put("Main-Class", "com.example.demo.ApplicationRunner")
//    }
//    baseName = project.name + "-all"
//}