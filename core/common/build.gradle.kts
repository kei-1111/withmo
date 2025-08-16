plugins {
    alias(libs.plugins.withmo.android.library)
    alias(libs.plugins.withmo.hilt)
}

android {
    namespace = "io.github.kei_1111.withmo.core.common"
}

val unityLibraryDir = File(rootDir, "libs/unity")

dependencies {
    implementation(fileTree(mapOf("dir" to unityLibraryDir, "include" to listOf("*.aar"))))
}