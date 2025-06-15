import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

allprojects {
    group = "${property("projectGroup")}"
    version = "${property("applicationVersion")}"

    repositories {
        mavenCentral()
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.getByName("jar") {
        enabled = true
    }

    java.sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        testImplementation("org.assertj:assertj-core:3.13.2")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "${project.property("javaVersion")}"
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}