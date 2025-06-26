plugins {
    alias(libs.plugins.withmo.android.library)
    alias(libs.plugins.withmo.hilt)
}

android {
    namespace = "io.github.kei_1111.withmo.core.service"
}

val unityLibraryLibsDir = project(":unityLibrary").projectDir.resolve("libs")

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.util)
    implementation(projects.unityLibrary)
    implementation(fileTree(mapOf("dir" to unityLibraryLibsDir, "include" to listOf("*.jar"))))
}
