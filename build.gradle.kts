plugins {
    id("org.spongepowered.gradle.sponge.dev") version "2.1.1"
    id("net.kyori.indra.publishing.sonatype") version "3.2.0"
    id("net.kyori.indra.crossdoc") version "3.2.0"
}

defaultTasks("build")

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public") {
        name = "sponge"
    }
}

val specVersion: String by project
tasks {
    val version = project.version.toString()
    withType(JavaCompile::class).configureEach {
        doFirst {
            options.compilerArgs.addAll(listOf("--module-path", classpath.asPath, "--module-version", version))
        }
    }

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
            (this as StandardJavadocDocletOptions).apply {
                links(
                    "https://logging.apache.org/log4j/2.x/javadoc/log4j-api/",
                    "https://checkerframework.org/api/",
                    "https://maven.apache.org/ref/3.8.6/maven-artifact/apidocs",
                    "https://jd.spongepowered.org/plugin-meta/0.8.2/"
                )
            }
        }

        doFirst {
            options.modulePath(classpath.toList())
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

indra {
    javaVersions().target(21)
}

indraCrossdoc {
    baseUrl(providers.gradleProperty("javadocPublishRoot"))
}

dependencies {
    api("org.spongepowered:plugin-meta:0.8.2")
    api("org.apache.maven:maven-artifact:3.8.6")
    api("org.apache.logging.log4j:log4j-api:2.17.0")
    compileOnlyApi("org.checkerframework:checker-qual:3.26.0")
}


