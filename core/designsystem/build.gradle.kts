plugins {
    alias(libs.plugins.withmo.android.library.compose)
}

android {
    namespace = "io.github.kei_1111.withmo.core.designsystem"
}

dependencies {
    implementation(libs.accompanist.drawablepainter)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.kotlinx.collections.immutable)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(projects.core.util)
}