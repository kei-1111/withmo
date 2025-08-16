import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File

class UnityLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val unityLibraryDir = File(rootDir, "libs/unity")
            
            dependencies {
                add("implementation", fileTree(mapOf("dir" to unityLibraryDir, "include" to listOf("*.aar"))))
            }
        }
    }
}