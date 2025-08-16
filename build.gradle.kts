// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.serialization) apply false

    // withmo
    alias(libs.plugins.withmo.android.application) apply false
    alias(libs.plugins.withmo.android.feature) apply false
    alias(libs.plugins.withmo.android.library.compose) apply false
    alias(libs.plugins.withmo.android.library) apply false
    alias(libs.plugins.withmo.detekt) apply false
    alias(libs.plugins.withmo.hilt) apply false
    alias(libs.plugins.withmo.unity.library) apply false
}
