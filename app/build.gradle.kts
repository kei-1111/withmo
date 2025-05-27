import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

android {
    namespace = "io.github.kei_1111.withmo"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.kei_1111.withmo"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "1.0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Unity
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        vectorDrawables {
            useSupportLibrary = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val unityLibraryLibsDir = project(":unityLibrary").projectDir.resolve("libs")

dependencies {

    implementation(libs.accompanist.drawablepainter)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.common)
    implementation(projects.core.featurebase)
    implementation(projects.unityLibrary)
    implementation(fileTree(mapOf("dir" to unityLibraryLibsDir, "include" to listOf("*.jar"))))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
