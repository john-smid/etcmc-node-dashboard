import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.shadow)
    distribution
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.core)
    implementation(libs.ktor.cio)
    implementation(libs.guava)
    implementation(libs.coroutines)
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    implementation(libs.arrow.optics)
    implementation(libs.kotter)
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.datetime)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("etcmc.node.dashboard.DashboardKt")
    executableDir = "./"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<ShadowJar>("shadowJar") {
    distributions {
        archiveBaseName = "dashboard"
        main {
            distributionBaseName = "dashboard"
        }
    }
    archiveBaseName = "dashboard"
    archiveClassifier = ""
    minimize {
        exclude(dependency("ch.qos.logback:.*:.*"))
    }
}
