plugins {
    alias(libs.plugins.withmo.android.library.compose)
    alias(libs.plugins.serialization)
}

android {
    namespace = "io.github.kei_1111.withmo.core.ui"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.util)
}
