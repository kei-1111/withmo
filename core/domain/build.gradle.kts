plugins {
    alias(libs.plugins.withmo.android.library)
    alias(libs.plugins.withmo.hilt)
}

android {
    namespace = "io.github.kei_1111.withmo.core.domain"
}

dependencies {
    implementation(projects.core.model)
}
