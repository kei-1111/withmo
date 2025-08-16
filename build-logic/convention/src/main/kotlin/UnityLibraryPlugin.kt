import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File

class UnityLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val unityLibraryAar = File(rootDir, "libs/unity/unityLibrary-release.aar")
            
            dependencies {
                add("implementation", files(unityLibraryAar))
            }
        }
    }
}