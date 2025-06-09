plugins {
    alias(libs.plugins.withmo.android.library.compose)
}

android {
    namespace = "io.github.kei_1111.withmo.core.ui"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.util)
}
