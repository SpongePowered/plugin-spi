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
    `java-library`
    id("net.minecraftforge.gradle")
    id("net.minecrell.licenser") version "0.4"
}

apply {
    plugin("org.spongepowered.mixin")
}

defaultTasks("licenseFormat", "build")

group = "org.spongepowered"
version = "0.1.0-SNAPSHOT"

minecraft {
    mappings("snapshot", "20200119-1.14.3")
    runs {
        create("server") {
            workingDirectory(project.file("./run"))
            args.addAll(listOf("nogui", "--launchTarget", "sponge_server_dev"))
            main = "org.spongepowered.modlauncher.Main"
        }

        create("client") {
            environment("target", "client")
            workingDirectory(project.file("./run"))
            args.addAll(listOf("--launchTarget", "sponge_client_dev", "--version", "1.14.4", "--accessToken", "0"))
            main = "org.spongepowered.modlauncher.Main"
        }
    }
}

repositories {
    mavenLocal()
    jcenter()
    maven("https://repo.spongepowered.org/maven")
}

license {
    header = file("HEADER.txt")
    newLine = false
    ext {
        this["name"] = project.name
        this["organization"] = "SpongePowered"
        this["url"] = "https://www.spongepowered.org"
    }

    include("**/*.java", "**/*.groovy", "**/*.kt")
}

dependencies {
    minecraft("net.minecraft:joined:1.14.4")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:21.0")
    implementation("com.google.inject:guice:4.0")
    implementation("cpw.mods:modlauncher:4.1.+")
    implementation("org.ow2.asm:asm-commons:6.2")
    implementation("cpw.mods:grossjava9hacks:1.1.+")
    implementation("org.spongepowered:mixin:0.8+")
    implementation("org.spongepowered:configurate-json:3.6.1")
    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
}

tasks.jar {
    manifest {
        attributes(
                // The basics
                "Specification-Title" to "SpongePowered Plugin Subsystem",
                "Specification-Version" to archiveVersion.get(),
                "Specification-Vendor" to "SpongePowered",
                "Implementation-Title" to "plugin-spi",
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to "SpongePowered",

                // Test Plugin
                "Loader" to "java_sponge",
                "MixinConfigs" to "mixins.json"
        )
    }
}
