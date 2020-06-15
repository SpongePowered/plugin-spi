plugins {
    `kotlin-dsl`
    `java-library`
    idea
}

subprojects {
    dependencies {
        gradleApi()
        gradleKotlinDsl()
    }
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    jcenter()
    maven("https://repo.spongepowered.org/maven")
}
