rootProject.name = "kotlin-playground"

include(
    "clone-coding:mockk",
    "lecture",
    "project",
    "book",
)


pluginManagement {
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
//                "org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
            }
        }
    }
}