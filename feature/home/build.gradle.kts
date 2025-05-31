plugins {
    alias(libs.plugins.withmo.android.feature)
}

android {
    namespace = "io.github.kei_1111.withmo.feature.home"
}

dependencies {
    implementation(libs.accompanist.drawablepainter)
    implementation(projects.core.ui)
}