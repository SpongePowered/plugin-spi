buildscript {
    repositories {
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/maven")
    }
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
    id("net.minecraftforge.gradle")
}
apply {
    plugin("org.spongepowered.mixin")
}

minecraft {
    mappings("snapshot", "20200119-1.14.3")
    runs {
        create("server") {
            workingDirectory( project.file("../run"))
            mods {
                create("launch") {
                    source(project.sourceSets["main"])
                }
            }
        }
    }
    project.sourceSets["main"].resources
            .filter { it.name.endsWith("_at.cfg") }
            .files
            .forEach {
                accessTransformer(it)
                parent?.minecraft?.accessTransformer(it)
            }
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:28.0-jre")
    minecraft("net.minecraft:server:1.14.4")
    implementation("cpw.mods:modlauncher:4.1.+")
    implementation("org.ow2.asm:asm-commons:6.2")
    implementation("cpw.mods:grossjava9hacks:1.1.+")
    implementation("net.minecraftforge:accesstransformers:1.0.+:shadowed")
    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
}
