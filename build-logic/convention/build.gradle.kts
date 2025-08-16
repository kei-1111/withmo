import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "io.github.kei_1111.withmo.build_logic.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.detekt.gradle)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.withmo.android.application.get().pluginId
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.withmo.android.feature.get().pluginId
            implementationClass = "AndroidFeaturePlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.withmo.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.withmo.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryPlugin"
        }
        register("detekt") {
            id = libs.plugins.withmo.detekt.get().pluginId
            implementationClass = "DetektPlugin"
        }
        register("hilt") {
            id = libs.plugins.withmo.hilt.get().pluginId
            implementationClass = "HiltPlugin"
        }
        register("unityLibrary") {
            id = libs.plugins.withmo.unity.library.get().pluginId
            implementationClass = "UnityLibraryPlugin"
        }
    }
}
