plugins {
    alias(libs.plugins.withmo.android.library)
}

android {
    namespace = "io.github.kei_1111.withmo.core.model"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(projects.core.common)
}
