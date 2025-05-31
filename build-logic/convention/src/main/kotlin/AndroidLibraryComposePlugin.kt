import com.android.build.api.dsl.LibraryExtension
import io.github.kei_1111.withmo.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "withmo.android.library")

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}