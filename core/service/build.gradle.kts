plugins {
    alias(libs.plugins.withmo.android.library)
    alias(libs.plugins.withmo.hilt)
    alias(libs.plugins.withmo.unity.library)
}

android {
    namespace = "io.github.kei_1111.withmo.core.service"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.util)
}
