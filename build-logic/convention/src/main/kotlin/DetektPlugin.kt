import io.github.kei_1111.withmo.detektPlugins
import io.github.kei_1111.withmo.library
import io.github.kei_1111.withmo.libs
import io.github.kei_1111.withmo.setupDetekt
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class DetektPlugin() : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.gitlab.arturbosch.detekt")

            setupDetekt(extensions.getByType<DetektExtension>())

            dependencies {
                detektPlugins(libs.library("detekt.compose"))
                detektPlugins(libs.library("detekt.formatting"))
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget = JavaVersion.VERSION_17.toString()
                // タスクの並列実行を促進
                outputs.cacheIf { true }
            }
        }
    }
}