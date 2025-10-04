import io.github.kei_1111.withmo.implementation
import io.github.kei_1111.withmo.library
import io.github.kei_1111.withmo.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "withmo.android.library.compose")
            apply(plugin = "withmo.hilt")

            dependencies {
                implementation(project(":core:common"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:domain"))
                implementation(project(":core:featurebase"))
                implementation(project(":core:model"))
                implementation(project(":core:ui"))
                implementation(project(":core:util"))

                implementation(libs.library("androidx.activity.compose"))
                implementation(libs.library("androidx.hilt.navigation.compose"))
                implementation(libs.library("androidx.material.icons.extended"))
                implementation(libs.library("androidx.material3"))
                implementation(libs.library("androidx.navigation.compose"))
                implementation(libs.library("kotlinx.collections.immutable"))
            }
        }
    }
}