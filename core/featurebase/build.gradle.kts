plugins {
    alias(libs.plugins.withmo.android.library)
}

android {
    namespace = "io.github.kei_1111.withmo.core.featurebase"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}