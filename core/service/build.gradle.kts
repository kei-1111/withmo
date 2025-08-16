plugins {
    alias(libs.plugins.withmo.android.library)
    alias(libs.plugins.withmo.hilt)
}

android {
    namespace = "io.github.kei_1111.withmo.core.service"
}

val unityLibraryDir = File(rootDir, "libs/unity")

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.util)
    implementation(fileTree(mapOf("dir" to unityLibraryDir, "include" to listOf("*.aar"))))
}
