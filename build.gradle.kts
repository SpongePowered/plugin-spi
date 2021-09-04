plugins {
    id("org.spongepowered.gradle.sponge.dev") version "1.1.1"
    id("net.kyori.indra.publishing.sonatype") version "2.0.6"
}

defaultTasks("build")

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public") {
        name = "sponge"
    }
}

val specVersion: String by project
tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Specification-Title" to "plugin-spi",
                    "Specification-Vendor" to "SpongePowered",
                    "Specification-Version" to specVersion,
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version
            ))
        }
    }

    javadoc {
        options {
            isFailOnError = false
            (this as StandardJavadocDocletOptions).apply {
                links(
                        "https://logging.apache.org/log4j/log4j-2.8.1/log4j-api/apidocs/"
                )
            }
        }
    }
}

val projectUrl: String by project
spongeConvention {
    repository("plugin-spi") {
        ci(true)
        publishing(true)
    }
    mitLicense()

    licenseParameters {
        this["name"] = project.name
        this["organization"] = "SpongePowered"
        this["url"] = "https://www.spongepowered.org"
    }
}

dependencies {
    api("org.spongepowered:plugin-meta:0.8.0")
    api("org.apache.maven:maven-artifact:3.8.1")
    implementation("org.apache.logging.log4j:log4j-api:2.8.1")
    implementation("org.checkerframework:checker-qual:3.17.0")
}


