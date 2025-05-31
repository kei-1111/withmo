plugins {
    alias(libs.plugins.withmo.android.feature)
}

android {
    namespace = "io.github.kei_1111.withmo.feature.home"
}


dependencies {
    implementation(libs.accompanist.drawablepainter)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(projects.core.ui)
    implementation(projects.core.util)
}