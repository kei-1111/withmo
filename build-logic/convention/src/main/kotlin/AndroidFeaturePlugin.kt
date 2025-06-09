import io.github.kei_1111.withmo.implementation
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

                implementation(libs.findLibrary("androidx.activity.compose").get())
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.material.icons.extended").get())
                implementation(libs.findLibrary("androidx.material3").get())
                implementation(libs.findLibrary("kotlinx.collections.immutable").get())
            }
        }
    }
}