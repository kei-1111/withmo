package io.github.kei_1111.withmo

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true

        dependencies {
            implementation(platform(libs.library("androidx.compose.bom")))
            implementation(libs.library("androidx.ui.tooling.preview"))
            debugImplementation(libs.library("androidx.ui.tooling"))
        }
    }
}