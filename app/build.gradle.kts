plugins {
    alias(libs.plugins.withmo.android.application)
    alias(libs.plugins.withmo.unity.library)
    alias(libs.plugins.serialization)
}

if (gradle.startParameter.taskRequests.toString().contains("Prod")) {
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
    apply(plugin = libs.plugins.google.services.get().pluginId)
}

android {
    namespace = "io.github.kei_1111.withmo"

    defaultConfig {
        applicationId = "io.github.kei_1111.withmo"
        versionCode = 10
        versionName = "2.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Unity
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "withmo-dev")
        }
        create("prod") {
            dimension = "env"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.service)
    implementation(projects.core.ui)
    implementation(projects.core.util)
    implementation(projects.feature.home)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.setting)

    "prodImplementation"(platform(libs.firebase.bom))
    "prodImplementation"(libs.firebase.analytics)
    "prodImplementation"(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}
