plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.github.kei_1111.withmo.core.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

val unityLibraryLibsDir = project(":unityLibrary").projectDir.resolve("libs")

dependencies {

    implementation(libs.androidx.core.ktx)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(projects.unityLibrary)
    implementation(fileTree(mapOf("dir" to unityLibraryLibsDir, "include" to listOf("*.jar"))))
}