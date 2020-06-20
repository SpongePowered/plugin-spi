plugins {
    `java-library`
    `maven-publish`
    id("net.minecrell.licenser") version "0.4"
    signing
}

defaultTasks("build")

repositories {
    mavenLocal()
    jcenter()
    maven("https://repo.spongepowered.org/maven")
    maven("https://repo-new.spongepowered.org/maven")
}

val specVersion: String by project

val main by sourceSets

val jar by tasks.existing(Jar::class) {
    manifest {
        attributes(mapOf(
                "Specification-Title" to "plugin-spi",
                "Specification-Vendor" to "SpongePowered",
                "Specification-Version" to specVersion, // We are version 1 of ourselves
                "Implementation-Title" to project.name,
                "Implementation-Version" to version,
                "Created-By" to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")})"
        ))
    }
}

val javadoc by tasks.existing(Javadoc::class) {
    options {
        encoding = "UTF-8"
        charset("UTF-8")
        isFailOnError = false
        (this as StandardJavadocDocletOptions).apply {
            links?.addAll(
                    mutableListOf(
                            "http://www.slf4j.org/apidocs/",
                            "https://google.github.io/guava/releases/21.0/api/docs/",
                            "https://google.github.io/guice/api-docs/4.1/javadoc/",
                            "http://asm.ow2.org/asm50/javadoc/user/",
                            "https://docs.oracle.com/javase/8/docs/api/"
                    )
            )
        }
    }
}

val javadocJar by tasks.registering(Jar::class) {
    group = "build"
    classifier = "javadoc"
    from(javadoc)
}

val sourceJar by tasks.registering(Jar::class) {
    classifier = "sources"
    group = "build"
    from(sourceOutput)
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

val sourceOutput by configurations.registering

dependencies {
    api("org.spongepowered:plugin-meta:0.5.0-SNAPSHOT")
    implementation("com.google.guava:guava:21.0")
    implementation("com.google.inject:guice:4.0")
    implementation("org.apache.logging.log4j:log4j-api:2.8.1")
    implementation("org.checkerframework:checker-qual:3.4.1")
    main.allSource.srcDirs.forEach {
        add(sourceOutput.name, project.files(it.relativeTo(project.projectDir).path))
    }
    testImplementation("junit:junit:4.12")
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    // TODO - get signing jar to configure with ci/cd
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    sign(tasks["jar"])
}
val spongeSnapshotRepo: String? by project
val spongeReleaseRepo: String? by project

val projectUrl: String by project
val projectDescription: String by project

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/spongepowered/plugin-spi")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
        // Set by the build server
        maven {
            name = "spongeRepo"
            val repoUrl = if ((version as String).endsWith("-SNAPSHOT")) spongeSnapshotRepo else spongeReleaseRepo
            repoUrl?.apply {
                url = uri(this)
            }
            val spongeUsername: String? by project
            val spongePassword: String? by project
            credentials {
                username = spongeUsername ?: System.getenv("ORG_GRADLE_PROJECT_spongeUsername")
                password = spongePassword ?: System.getenv("ORG_GRADLE_PROJECT_spongePassword")
            }
        }
    }
    publications {
        register("sponge", MavenPublication::class) {
            artifact(jar.get())
            artifact(sourceJar.get())
            artifact(javadocJar.get())
            pom {
                this.name.set("plugin-spi")
                this.description.set(projectDescription)
                this.url.set(projectUrl)

                licenses {
                    license {
                        this.name.set("MIT")
                        this.url.set("https://opensource.org/licenses/MIT")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/SpongePowered/plugin-spi.git")
                    developerConnection.set("scm:git:ssh://github.com/SpongePowered/plugin-spi.git")
                    this.url.set(projectUrl)
                }
            }
        }
    }

}
