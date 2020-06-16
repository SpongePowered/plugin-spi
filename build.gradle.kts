plugins {
    // Apply the java-library plugin to add support for Java Library
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
            addStringOption("-Xdoclint:none", "-quiet")
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
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:21.0")
    implementation("com.google.inject:guice:4.0")
    implementation("org.apache.logging.log4j:log4j-api:2.8.1")
    implementation("org.checkerframework:checker-qual:3.4.1")
    main.allSource.srcDirs.forEach {
        add(sourceOutput.name, project.files(it.relativeTo(project.projectDir).path))
    }
    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    // TODO - get signing jar to configure with ci/cd
//    useInMemoryPgpKeys(signingKey, signingPassword)
//    sign(tasks["jar"])
}
val spongeRepo: String? by project

tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf {
        (repository == publishing.repositories["GitHubPackages"] &&
                publication == publishing.publications["gpr"]) ||
        (!spongeRepo.isNullOrBlank()
                && repository == publishing.repositories["spongeRepo"]
                && publication == publishing.publications["sponge"])

    }
}

tasks.withType<PublishToMavenLocal>().configureEach {
    onlyIf {
        publication == publishing.publications["sponge"]
    }
}
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
            spongeRepo?.apply {
                url = uri(this)
            }
            val spongeUsername: String? by project
            val spongePassword: String? by project
            credentials {
                username = spongeUsername ?: System.getenv("ORG_GRADLE_PROJECT_spongeUsername")
                password = spongePassword ?: System.getenv("ORG_GRADLE_PROJECT_spongePassword")
            }
            artifacts {
                jar.get()
                sourceJar.get()
                javadocJar.get()
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class) {
            from(components["java"])
        }
        register("sponge", MavenPublication::class) {
            artifact(jar.get())
            artifact(sourceJar.get())
            artifact(javadocJar.get())
        }
    }

}
