plugins {
    alias(libs.plugins.withmo.android.feature)
}

android {
    namespace = "io.github.kei_1111.withmo.feature.setting"
}


dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(projects.core.util)
}