import io.github.kei_1111.withmo.implementation
import io.github.kei_1111.withmo.library
import io.github.kei_1111.withmo.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "com.google.dagger.hilt.android")

            dependencies {
                "ksp"(libs.library("hilt.android.compiler"))
                implementation(libs.library("hilt.android"))
            }
        }
    }
}