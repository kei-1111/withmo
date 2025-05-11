import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
//    detekt
    alias(libs.plugins.detekt)

//    KSP
    alias(libs.plugins.ksp)

//    Hilt
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.github.kei_1111.withmo"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.kei_1111.withmo"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "1.0.5"

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    Immutable
    implementation(libs.kotlinx.collections.immutable)

//    Material Icon Extended
    implementation(libs.androidx.material.icons.extended)

//    Accompanist Permissions
    implementation(libs.accompanist.permissions)

//    Accompanist DrawablePainter
    implementation(libs.accompanist.drawablepainter)

//    Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)

//    DataStore
    implementation(libs.androidx.datastore.preferences)

//    Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

//    Unity
    implementation(project(":unityLibrary"))
    implementation(fileTree(mapOf("dir" to unityLibraryLibsDir, "include" to listOf("*.jar"))))

//    detekt
    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
}

detekt {
    config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true

    source = files("src/main/java")

    tasks {
        withType<Detekt> {
            reports {
            }
        }
    }

    autoCorrect = true
}
